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
        
        public TestRTD()
        {
            
        }

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
            result[0,0] = _topicID;
            result[1,0] = _eventData;

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
            _eventData = "Message ID: " + e.Pono.ID + " ('"+e.Pono.MSG+"') was set to Done!";
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




