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

