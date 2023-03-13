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

