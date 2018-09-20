using System;
using System.Collections.Generic;
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
            builder.Append( "Id:" + ID +
                            " ,Msg: " + MSG +
                            " ,Status: " + STATUS);
            return builder.ToString();
        }
    }
}
