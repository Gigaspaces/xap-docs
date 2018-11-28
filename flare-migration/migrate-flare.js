/**
 * ScriptName: migrate-flare.js
 * By: Shlomo Sagir, "Tech-Tav Documentation, Ltd."
 * Copyright 2018 (All rights reserved)
 * Command Line: cscript.exe migrate-flare.js //Nologo PathToOutputFolder
 **/

var terms = [{
    "find": /<html xmlns/g,
    "replace": '<?xml version="1.0" encoding="utf-8"?>\r\n<html xmlns'
  },
  {
    "find": /<b>(.*?)<\/b>/g,
    "replace": '<span class="tc-bold">$1</span>'
  },
  {
    "find": /<strong>(.*?)<\/strong>/g,
    "replace": '<span class="tc-bold">$1</span>'
  },
  {
    "find": /<em>(.*?)<\/em>/g,
    "replace": '<span class="tc-italic">$1</span>'
  },
  {
    "find": /&rsquo;/g,
    "replace": "'"
  },
  {
    "find": /&(\w{2}quo|quot);/g,
    "replace": '"'
  },
  {
    "find": /&mdash;/g,
    "replace": '—'
  },
  {
    "find": /&ndash;/g,
    "replace": '–'
  },
  {
    "find": /&hellip;/g,
    "replace": '...'
  },
  {
    "find": /&rarr;/g,
    "replace": '&#8592;'
  },
  {
    "find": /&frasl;/g,
    "replace": '&#8260;'
  },
  {
    "find": /&nbsp;/g,
    "replace": ' '
  },
  {
    "find": /&reg;/g,
    "replace": '&#174;'
  },
  {
    "find": /<br>/g,
    "replace": '<br/>'
  },
  {
    "find": /&amp;#35;/g,
    "replace": '#'
  },
  {
    "find": /width=(\d)/g,
    "replace": 'width="$1'
  },
  {
    "find": /<\/?nobr>/g,
    "replace": ''
  },
  {
    "find": /"\.\//g,
    "replace": '"'
  },
  {
    "find": /\.html/g,
    "replace": '.htm'
  },
  {
    "find": /<h(\d) id="(.+?)">/g,
    "replace": '<h$1><a name="$2">&#160;</a>'
  },
  {
    "find": /<\/(table|thead|tbody|tr|th|td|div|ul)>[\s\s]*?<\/p>/g,
    "replace": '</$1>'
  },
  {
    "find": /<p>[\s\s]*?<(table|thead|tbody|tr|th|td|div|ol|ul)/g,
    "replace": '<$1'
  },
  {
    "find": /<p>[\s\s]*?<p>/g,
    "replace": '<p>'
  },
  {
    "find": /<p>[\s\s]*?<\/div>/g,
    "replace": '</div>'
  },
  {
    "find": /<\/p>[\s\s]*?<\/p>/g,
    "replace": '</p>'
  },
  {
    "find": /<p class="tc-admon-title">(Note|Tip|Attention|Important)<\/p>/g,
    "replace": ''
  }
];

var special = [
  {
    find: /Resources\/Snippets\//g,
    replace: 'Resources/Snippets/',
    pattern: '../',
    depthMod: 0
  },
  {
    find: /\/attachment_files/g,
    replace: 'Resources/Static/attachment_files',
    pattern: '../',
    depthMod: 0
  }
];

var debugMode = false;
var logFile = "";
var logLevel = 1;
var sExt = "html",
  iExt = sExt.length;
var sError = WScript.ScriptName + "::Error: ";
var fso = new ActiveXObject("Scripting.FileSystemObject");
var wsh = new ActiveXObject("WScript.Shell");
var oArgs = WScript.Arguments;
var sFolder = (oArgs.length === 1) ? oArgs(0) : null;

if (!/^\w:\\/.test(sFolder)) {
  sFolder = wsh.currentDirectory +
    ((sFolder.substr(0, 1) === "\\") ? "" : "\\") +
    sFolder;
}

initUtils();

lfh = fso.CreateTextFile(sFolder + "\\migrate-flare.log.txt", true);
log('sFolder:: ', sFolder);
if (sFolder) {
  log(WScript.ScriptName + ":: Started");
  log("Folder::" + sFolder);
  processFolder(sFolder, true);
  log(WScript.ScriptName + ":: Finished");
} else
  log(sError + "Missing Parameter - You must specify the path to the topics folder in the Projects Content folder");
lfh.Close();

function processFolder(sFolder, deep) {
  var f, fc, fn;

  if (!fso.FolderExists(sFolder) || !(f = fso.GetFolder(sFolder))) {
    log(sError + "Unable to open topics folders");
    return;
  }

  if (deep) {
    for (fc = new Enumerator(f.SubFolders); !fc.atEnd(); fc.moveNext()) {
      processFolder(sFolder + "\\" + fc.item().name, deep);
    }
  }

  for (fc = new Enumerator(f.files); !fc.atEnd(); fc.moveNext()) {
    fn = fc.item().name;
    if (fn.substr(fn.length - iExt, iExt) === sExt) {
      writeHTM(sFolder + "\\" + fn, processHTM(sFolder + "\\" + fn));
    }
  }
}

function processHTM(sFilename) {
  var s = t = readHTM(sFilename);
  for (var i = 0; i < terms.length; i++) {
    s = s.replace(terms[i].find, terms[i].replace);
  }

  var depth = sFilename.substr(sFolder.length + 1).split('\\').length - 1;
  if (depth) {
    for (var j = 0; j < special.length; j++) {
      s = s.replace(special[j].find, special[j].pattern.repeat(depth+special[j].depthMod)+special[j].replace);
    }
  }
  return {
    "html": s,
    "writeReq": s !== t
  };
}

function readHTM(sFilename) {
  var ForReading = 1;
  var fh = fso.OpenTextFile(sFilename, ForReading);
  var s = fh.ReadAll();

  fh.Close();

  return s;
}

function writeHTM(sFilename, result) {
  var fh;

  log("  " + sFilename + " [" + (result.writeReq ? "Modified" : "Unchanged") + ']');
  if (!result.writeReq) return;

  if (debugMode) {
    log(WScript.ScriptName + "::\n[" + sFilename + "]:\n" + result.html + "\n", 2);
    return;
  }

  if (fso.FileExists(sFilename)) {
    fh = fso.GetFile(sFilename);
    fh.Delete();
  }

  sFilename = sFilename.substr(0, sFilename.length - 1);
  fh = fso.CreateTextFile(sFilename, true);
  fh.Write(result.html);
  fh.Close();
}

function log(sMessage, iLevel) {
  var l = iLevel || 1;
  if (logLevel >= l) {
    WScript.Echo(sMessage);
    lfh.WriteLine(sMessage);
  }
}

function initUtils() {
  if (!String.prototype.repeat) {
    String.prototype.repeat = function (count) {
      'use strict';
      if (this == null) {
        throw new TypeError('can\'t convert ' + this + ' to object');
      }
      var str = '' + this;
      count = +count;
      if (count != count) {
        count = 0;
      }
      if (count < 0) {
        throw new RangeError('repeat count must be non-negative');
      }
      if (count == Infinity) {
        throw new RangeError('repeat count must be less than infinity');
      }
      count = Math.floor(count);
      if (str.length == 0 || count == 0) {
        return '';
      }
      // Ensuring count is a 31-bit integer allows us to heavily optimize the
      // main part. But anyway, most current (August 2014) browsers can't handle
      // strings 1 << 28 chars or longer, so:
      if (str.length * count >= 1 << 28) {
        throw new RangeError('repeat count must not overflow maximum string size');
      }
      var rpt = '';
      for (var i = 0; i < count; i++) {
        rpt += str;
      }
      return rpt;
    };
  }
}