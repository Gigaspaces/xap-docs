using System;
using System.Collections.Generic;
using System.Text;

using System.Runtime.InteropServices;
using Microsoft.Win32;

using GigaSpaces.Core;
using GigaSpaces.Core.Exceptions;
using GigaSpaces.Core.Events;
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
