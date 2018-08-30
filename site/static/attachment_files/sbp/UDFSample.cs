using System;
using System.Collections.Generic;
using System.Text;

using System.Runtime.InteropServices;
using Microsoft.Win32;

using GigaSpaces.Core;
using GigaSpaces.Core.Exceptions;
using GigaSpaces.Core.Events;
using System.Configuration;


//-----------------------------------------------------------
// This class demonstrate how to build a UDF wrapper of a GigaSpaces .NET proxy.
//
// We show how to wrap a Read and Update space operations to be used from within
// the Excel spreadsheet as a regular Excel functions
//
// All public methods are exposed to the Excel user and after the class is registered and added as an "add-in" will apear in the Excel functions list
//-----------------------------------------------------------
namespace GSExcelLib
{
    [ClassInterface(ClassInterfaceType.AutoDual), ComVisible(true)] //required to tag a C# class a UDF
    public class TickService
    {
        private ISpaceProxy _proxy;

    public TickService()
        {

        }

        public String GetTickInfo(string symbol)
        {
            bool isConnected = SpaceInit(); 
            if (!(isConnected))
                return "connection error";

            TickInfo.TickInfo tick = ReadTick(symbol);
            if (tick == null)
                return ("Not Exist");
            return tick.ToString();
         }

        public String UpdateLastPrice(string symbol, double last)
        {          
            bool isConnected = SpaceInit();
            if (!(isConnected))
                return "connection error";

            TickInfo.TickInfo tick = ReadTick(symbol);
            if (tick == null)
                return ("Not Exist");

            tick.Last = last;
            _proxy.Update<TickInfo.TickInfo>(tick);
            return "Symbol " + symbol + " has been Updated";
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

        private TickInfo.TickInfo ReadTick(String symbol)
        {
            // reading the TickInfo from the Space

            TickInfo.TickInfo template = new TickInfo.TickInfo();
            template.Symbol = symbol;

            TickInfo.TickInfo tick = _proxy.Read<TickInfo.TickInfo>(template);
            return tick;
        }
    }
}
