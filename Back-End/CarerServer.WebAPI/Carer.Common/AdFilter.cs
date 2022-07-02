using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Carer.Common
{
    public class AdFilter
    {
        private byte type;
        private Guid currentUser;
        private Guid? userId = null;
        private bool showAppliedCount = false;
        private bool showStatus = false;

        public byte Type { get => type; set => type = value; }
        public Guid CurrentUser { get => currentUser; set => currentUser = value; }

        public Guid? UserId { get => userId; set => userId = value; }
        public bool ShowAppliedCount { get => showAppliedCount; set => showAppliedCount = value; }
        public bool ShowStatus { get => showStatus; set => showStatus = value; }
    }
}
