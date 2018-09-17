---
type: post
title:  RTD and UDF Examples - GigaSpaces-Excel Integration
categories: SBP
parent: excel-that-scales-solution.html
weight: 700
---



|Author|XAP Version|Last Updated | Reference | Download |
|------|-----------|-------------|-----------|----------|
|Pini Cohen| 6.6 |   |           |          |



# Overview

This section includes basic and advanced code examples for using the GigaSpaces-Excel integration with Microsoft Excel User-Defined Functions (UDF) and Real-Time Data (RTD).

# Basic Examples


## HelloUDF -- Performing Excel Functions in Space


```java
using System;
using System.Runtime.InteropServices;
using Microsoft.Win32;

using GigaSpaces.Core;
using System.Configuration;

using HelloCommon;

//-----------------------------------------------------------
// This class demonstrate how to build a UDF wrapper of a GigaSpaces .NET proxy.
//
// We show how to wrap space operations to be used from within
// the Excel spreadsheet as a regular Excel functions
//
// All public methods are exposed to the Excel user and after the class is registered and added as an "add-in" will apear in the Excel functions list
//-----------------------------------------------------------
namespace HelloUDF
{
    [ClassInterface(ClassInterfaceType.AutoDual), ComVisible(true)] //required to tag a C# class a UDF
    public class HelloUDF
    {
        private ISpaceProxy _proxy;

        public HelloUDF()
        {
            SpaceInit();
        }

        public String AddMsg(int id, string msg)
        {
            String output;

            HelloMsg theMsg = new HelloMsg();
            theMsg.ID = id;
            theMsg.MSG = msg;
            theMsg.STATUS = "working";

            try
            {
                _proxy.Update<HelloMsg>(theMsg,
                                        null, //no transactions
                                        long.MaxValue, 0, //lease and write timeouts
                                        UpdateModifiers.UpdateOrWrite);

                output = "["+ theMsg.ToString() + "] was written to the space";
            }
            catch (Exception e)
            {
                output = e.Message;
            }
            return output;
        }

        public String SetToDone(int id)
        {
            String output;

            HelloMsg theMsg = new HelloMsg();
            theMsg.ID = id;
            theMsg.STATUS = "done";

            try
            {
                _proxy.Update<HelloMsg>(theMsg,
                                        null, //no transactions
                                        long.MaxValue, 0, //lease and write timeouts
                                        UpdateModifiers.PartialUpdate);
                output = "setting Messgae ID " + id + " to done...";
            }
            catch (Exception e)
            {
                output = e.Message;
            }
            return output;
        }

        [ComRegisterFunctionAttribute] //required to tag a C# class a UDF
        public static void RegisterFunction(Type type)
        {
            Registry.ClassesRoot.CreateSubKey(GetSubKeyName(type));
        }

        [ComUnregisterFunctionAttribute] //required to tag a C# class a UDF
        public static void UnregisterFunction(Type type)
        {
            Registry.ClassesRoot.DeleteSubKey(GetSubKeyName(type), false);
        }

        private static string GetSubKeyName(Type type) //required to tag a C# class a UDF
        {
            string s = @"CLSID\{" + type.GUID.ToString().ToUpper() + @"}\Programmable";
            return s;
        }

        private bool SpaceInit()
        {
            try
            {
                string url = ConfigurationManager.AppSettings["SpaceUrl"]; //an environment variable read from the excel.exe.config file
                _proxy = SpaceProxyProviderFactory.Instance.FindSpace(url);
                _proxy.OptimisticLocking = true;
                return true;
            }
            catch (Exception ex)
            {
                ex.ToString();
                return false;
            }
        }
    }
}
```

## HelloRTD -- Loading Data from Space to Excel


```java
using System;
using System.Configuration;
using System.Runtime.InteropServices;//<-- For our interop attributes.
using Microsoft.Office.Interop.Excel;

using GigaSpaces.Core;
using GigaSpaces.Core.Events;

using HelloCommon;

//-----------------------------------------------------------
// This class demonstrate how to build a RTD wrapper to "push" data from
// GigaSpaces to the Excel spreadsheet
//
// We show how to register on a notification for a change, and then push
// this change to a cell in Excel
//
//  Usage of the RTD function within Excel:
//  =RTD("HelloRTD",,)
//
//-----------------------------------------------------------

namespace HelloRTD
{
	[ComVisible(true), ProgId("HelloRTD")] //the name of the progId parameter in the RTD excel function
	public class TestRTD : IRtdServer//this is the interface any C# class must implement to become a RTD
	{
		private IRTDUpdateEvent _xlRTDUpdate; //the type of object to link between this runtime and the Excel
		private ISpaceProxy _proxy; //space proxy
		private int _topicID; //Excel cell id to which we will "wire" the notification
		private string _eventData; //the data we receive from the space upon notification
		IEventRegistration _eventReg; //store the space notification as local object to de-register

		#region IRTD Members
		/**
         * this method is called by Excel when the RTD server start (the first time the workbook calls =RTD(...)
		 * here we should creat the connection to the space
         * CallbackObject - the link between this runtime and the Excel
        */
		public int ServerStart(IRTDUpdateEvent CallbackObject)
		{
			// Hold a reference to the callback object.
			_xlRTDUpdate = CallbackObject;

			bool isConnected = SpaceInit(); //connect to the space

			//All is well, return 1.
			return (isConnected ? 1 : 0);
		}
		/**
		 * called when closing the Excel
		 * */
		public void ServerTerminate()
		{
			clean();
		}
		/**
		 * topicID - unique ID of the cell in the Excel
		 * RTDparms - must have at least one parameter
		 * getNewValues - used by the Excel, no need change
		 */
		public object ConnectData(int topicID, ref Array RTDparms, ref bool getNewValues)
		{
			// Registering for notifications on status "done"
			HelloMsg notifyTemplate = new HelloMsg();
			notifyTemplate.STATUS = "done";
			_eventReg = _proxy.DefaultDataEventSession.AddListener
						<HelloMsg>(notifyTemplate, Space_DataChanged);

			//store the cell this RTD is written in
			_topicID = topicID;
			return "This cell is listening for msg with status 'done' ...";
		}
		/**
		 * called when deleteing the RTD function from Excel
		 */

		public void DisconnectData(int topicID)
		{
			clean();
		}
		/**
		 * called by the Excel when _xlRTDUpdate.UpdateNotify() is called
		 * since we call UpdateNotify in each space event,
		 * RefreshData is called for each event seperately
		 *
		 * topicCount = tell the excel how many cells we updated
		 * */
		public Array RefreshData(ref int topicCount)
		{
			// This method returns a two-dimensional array of Variant values.
			// The first dimension represents a list of topic IDs;
			// these topic IDs map to the TopicID parameter in the ConnectData method above.
			// This is how Excel associates topics with data.
			// The second dimension represents the values associated with the topic IDs.

			//1st dimention is always 2 - since we have topic and value
			//2nd dimention is the number of cells we want to update in the Excel
			//    as a result of this specific event
			//
			//  |topicID(0,0)|topicID(0,1)|topicID(0,2)|... topicID(0,n)|
			//  |value1 (1,0)|value2 (1,1)|value3 (1,2)|... valueN (1,n)|
			//
			// n = the number of cells we update in each event
			//

			//since we always update a single cell, we build the following array
			object[,] result = new object[2, 1];
			result[0, 0] = _topicID;
			result[1, 0] = _eventData;

			topicCount = 1;
			return result;
		}

		public int Heartbeat()
		{
			return 1; // the RTD server is up and running, 0 indicates error
		}

		#endregion

		/**
         * calleback by GigaSpaces when event occured
         * EventArgs is the data of the event
         */

		private void Space_DataChanged(object sender, SpaceDataEventArgs<HelloMsg> e)
		{
			//store in the _receivedData the data we got from the event
			_eventData = "Message ID: " + e.Object.ID + " ('" + e.Object.MSG + "') was set to Done!";
			//Tell Excel that we have updates. Then the Excel calls back to RefreshData
			_xlRTDUpdate.UpdateNotify();
		}

		private bool SpaceInit()
		{
			try
			{
				string url = ConfigurationManager.AppSettings["SpaceUrl"];
				_proxy = SpaceProxyProviderFactory.Instance.FindSpace(url);
				_proxy.OptimisticLocking = true;
				return true;
			}
			catch (Exception ex)
			{
				System.Console.Write(ex);
				return false;
			}

		}

		private void clean()
		{
			try
			{
				//remove the event
				if (_eventReg != null)
				{
					_proxy.DefaultDataEventSession.RemoveListener(_eventReg);
					_eventReg = null;
				}
				// Clear the RTDUpdateEvent reference.
				_xlRTDUpdate = null;
			}
			catch (Exception ex)
			{
				System.Console.Write(ex);
			}
		}
	}
}
```

## HelloMsg


```java
using System.Text;

using GigaSpaces.Core.Metadata;

namespace HelloCommon
{
	[SpaceClass(IncludeProperties = IncludeMembers.Public, IncludeFields = IncludeMembers.None)]

	public class HelloMsg
	{
		private int _id;
		private string _msg;
		private string _status;

		public HelloMsg()
		{
			_id = -1;
		}

		// NullValue instructs the space to consider a value as null when an object instance is  used as a template
		[SpaceID, SpaceRouting, SpaceProperty(Index = SpaceIndexType.Basic, NullValue = -1)]
		public int ID
		{
			get { return _id; }
			set { _id = value; }
		}

		public string MSG
		{
			get { return _msg; }
			set { _msg = value; }
		}

		public string STATUS
		{
			get { return _status; }
			set { _status = value; }
		}

		public override string ToString()
		{
			StringBuilder builder = new StringBuilder();
			builder.Append("Id:" + ID +
							" ,Msg: " + MSG +
							" ,Status: " + STATUS);
			return builder.ToString();
		}
	}
}
```

## RTDSample -- Loading Data from Space to Excel


```java
using System;
using System.Collections;
using System.Text;
using System.Runtime.InteropServices;//<-- For our interop attributes.

using Microsoft.Office.Interop.Excel;
using GigaSpaces.Core;
using GigaSpaces.Core.Exceptions;
using GigaSpaces.Core.Events;
using System.Collections.Generic;
using System.Configuration;

//-----------------------------------------------------------
// This class demonstrate how to build a RTD wrapper to "push" data from
// GigaSpaces to the Excel spreadsheet
//
// We show how to register on a notification of a tick change, and then push
// the tick value to the Excel
//
//  Usage of the RTD function within Excel:
//  =RTD("GSFeader",,[the tick symbol we want to monitor])
//
//-----------------------------------------------------------
namespace GSStreamer
{
    [ComVisible(true), ProgId("GSFeeder")] //the name of the progId parameter in the RTD excel function
    public class GSStreamer : IRtdServer//this is the interface any C# class must implement to become a RTD
    {
        private IRTDUpdateEvent _xlRTDUpdate;
        private ISpaceProxy _proxy;
        private Dictionary<int, TopicTick> _topicIDTable; //used to map between the Excel cellID and the returned data
        private Dictionary<string, TopicTick> _tickTable;

        public GSStreamer()
        {
            _topicIDTable = new Dictionary<int, TopicTick>();
            _tickTable = new Dictionary<string, TopicTick>();
        }

        #region IRTD Members
		//this method is called by Excel when the RTD server start (the first time the workbook calls =RTD(...)
		//here we should creat the connection to the space
        public int ServerStart(IRTDUpdateEvent CallbackObject)
        {
            // Hold a reference to the callback object.
            _xlRTDUpdate = CallbackObject;

            bool isConnected = SpaceInit();

            //All is well, return 1.
            return (isConnected ? 1 : 0);
        }
        public void ServerTerminate()
        {
            // Clear the RTDUpdateEvent reference.
            _xlRTDUpdate = null;
        }

        //this method is called by Excel for every =RTD(...) execution
		//the topicID is a unique identifier for every cell in the excel, we use it to map where to notify back to
		public object ConnectData(int topicID, ref Array RTDparms, ref bool getNewValues)
        {
            string symbol = (string)RTDparms.GetValue(0);
            // Reading from the Space
            string tickInfo = ReadTick(symbol);

            // Registering for notifications using the space proxy
            TickInfo.TickInfo notifyTemplate = new TickInfo.TickInfo();
            notifyTemplate.Symbol = symbol;
            IEventRegistration eventReg = _proxy.DefaultDataEventSession.AddListener<TickInfo.TickInfo>(notifyTemplate, Space_TickChanged);

            //creating a new topic (which has a referance to the listner) and store it locally
            TopicTick tp = new TopicTick(topicID, symbol, eventReg);

            if (!_tickTable.ContainsKey(symbol))
                _tickTable.Add(symbol, tp);
            if (!_topicIDTable.ContainsKey(topicID))
                _topicIDTable.Add(topicID, tp);

            return (object)tickInfo;
        }
		//this method is called by the Excel when a user erase and RTD method from a given cell.
        public void DisconnectData(int topicID)
        {
            try
            {
                TopicTick tp = _topicIDTable[topicID];
                _topicIDTable.Remove(tp.TopicID);
                _tickTable.Remove(tp.symbol);
                _proxy.DefaultDataEventSession.RemoveListener(tp.EventReg);
            }
            catch (Exception ex)
            {
                ex.ToString();
            }
        }

        public Array RefreshData(ref int topicCount)
        {
            // This method returns a two-dimensional array of Variant values.
            // The first dimension represents a list of topic IDs;
            // these topic IDs map to the TopicID parameter in the ConnectData method above.
            // This is how Excel associates topics with data.
            // The second dimension represents the values associated with the topic IDs.

            object[,] result = new object[2, _topicIDTable.Count];

            //build a array of all the topics that changed to send the Excel
            foreach (TopicTick tp in _topicIDTable.Values)
            {
                if (tp.Changed)
                {
                    tp.Changed = false;
                    result[0, topicCount] = tp.TopicID;
                    result[1, topicCount] = ReadTick(tp.symbol); // we can use the symbol attribute: symbol.Pono instead of reading from space
                    topicCount++;  //Tell Excel how many topics we updated.
                }
            }

            //Return the updates.
            return result;
        }

        public int Heartbeat()
        {
            return 1; // the RTD server is up and running, 0 indicates error
        }

        #endregion

        //this method is invoked when a tick has changed
        private void Space_TickChanged(object sender, SpaceDataEventArgs<TickInfo.TickInfo> e)
        {
            //bookmark the tick that was changed
            _tickTable[e.Pono.Symbol].Changed = true;
            //Tell Excel that we have updates,
            //as a result the excel will call the RefreshData function
            _xlRTDUpdate.UpdateNotify();
        }

        private bool SpaceInit()
        {
            try
            {
                string url = ConfigurationManager.AppSettings["SpaceUrl"]; //an environment variable read from the excel.exe.config file
                _proxy = SpaceProxyProviderFactory.Instance.FindSpace(url);
                _proxy.OptimisticLocking = true;
                return true;
            }
            catch (Exception ex)
            {
                ex.ToString();
                return false;
            }
        }

        private string ReadTick(String symbol)
        {
            // reading the Person Data from the Spaces

            TickInfo.TickInfo template = new TickInfo.TickInfo();
            template.Symbol = symbol;

            TickInfo.TickInfo tick = _proxy.Read<TickInfo.TickInfo>(template);

            if (tick == null)
                return ("Not Exist");
            else
                return tick.ToString();
        }

        private class TopicTick
        {
            public int TopicID;
            public bool Changed;
            public string symbol;
            public IEventRegistration EventReg;

            public TopicTick(int topicId, string symbol, IEventRegistration eventReg)
            {
                this.TopicID = topicId;
                this.Changed = false;
                this.symbol = symbol;
                this.EventReg = eventReg;
            }
        }
    }
}
```

## TickInfo


```java
using System;
using System.Collections.Generic;
using System.Text;

using GigaSpaces.Core.Metadata;

namespace TickInfo
{
    // SpaceClass(IncludeProperties = IncludeMembers.Public) instructs the space to take only public properties
    // SpaceClass(IncludeFields = IncludeMembers.None) instructs the space not to take any fields
    [SpaceClass(IncludeProperties = IncludeMembers.Public, IncludeFields = IncludeMembers.None)]
    public class TickInfo
    {
        private string _symbol;
        private double _open;
        private double _close;
        private double _last;

        // SpaceID is used to indicate a unique field value. This field value is used to generate the Entry UID
        // SpaceProperty(Index = SpaceIndexType.Basic) is used indicate an indexed field
        [SpaceID, SpaceRouting, SpaceProperty(Index = SpaceIndexType.Basic)]
        public string Symbol
        {
            get { return _symbol; }
            set { _symbol = value; }
        }
        [SpaceProperty(NullValue = -1)]
        public double Open
        {
            get { return _open; }
            set { _open = value; }
        }
        [SpaceProperty(NullValue = -1)]
        public double Close
        {
            get { return _close; }
            set { _close = value; }
        }
        [SpaceProperty(NullValue = -1)]
        public double Last
        {
            get { return _last; }
            set { _last = value; }
        }

        public TickInfo()
        {
            this._open = -1;
            this._close = -1;
            this._last = -1;
        }

        public override string ToString()
        {
            StringBuilder builder = new StringBuilder();
            builder.Append(" Symbol: " + Symbol);
            builder.Append(" Open: " + Open.ToString());
            builder.Append(" Close: " + Close.ToString());
            builder.Append(" Last: " + Last.ToString());
            return builder.ToString();
        }
    }
}
```


# What's Next?

{{% refer %}}
[Write Your First RTD/UDF Application](./writing-your-first-rtd-or-udf-application.html)
[See the Market-Data Example](./gigaspaces-excel-market-data-example.html)
Back to The [Excel that Scales Solution](./excel-that-scales-solution.html) section.
{{% /refer %}}
