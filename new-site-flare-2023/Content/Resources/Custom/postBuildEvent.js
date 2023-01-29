/**
/**
 * ScriptName: postBuildEvent.js
 * By: Shlomo Sagir, "Tech-Tav Documentation, Ltd."
 * Copyright 2018 (All rights reserved)
 * Command Line: cscript.exe postBuildEvent.js //Nologo PathToOutputFolder
 **/

var logLevel = 2;
var sError = WScript.ScriptName + "::Error: ";
var fso = new ActiveXObject("Scripting.FileSystemObject");
var wsh = new ActiveXObject("WScript.Shell");
var sFolder = getFolder(WScript.Arguments);
lfh = fso.CreateTextFile(sFolder + "\\postBuildEvent.log", true);
if (sFolder) {
  log(WScript.ScriptName + ":: Started");
  processBuildFolder(fso.GetFolder(sFolder));
  log(WScript.ScriptName + ":: Finished");
} else {
  log(sError + "Missing Parameter - You must specify the path to the topics folder in the Projects Content folder");
}
lfh.Close();

function getFolder(oArgs) {
  var result = (oArgs.length === 1) ? oArgs(0) : null;
  if (!/^\w:\\/.test(result)) {
    result = wsh.currentDirectory + ((result.substr(0, 1) === "\\") ? "" : "\\") + result;
  }
  return result;
}

function processBuildFolder(srcFolder) {
  var sVersion = srcFolder.Name.replace("InsightEdge-", "");
  var sOutputPath = srcFolder.ParentFolder.ParentFolder.ParentFolder.ParentFolder + "\\output";
  //var sDstPath = sOutputPath + "\\xap\\" + sVersion;
  var sDstPath = sOutputPath + "\\" + (sVersion === "14.0" ? "xap\\" + sVersion : sVersion);
  log("Copying " + srcFolder + " to " + sDstPath);
  fso.CopyFolder(srcFolder.Path, sDstPath);
  var dstFolder = fso.GetFolder(sDstPath);
  log("Processing HTML files...");
  var replaceTerms = [
    { "find": /=\"\S*?Resources\/Static\//g, "replace": '="/' },
    { "find": /https:\/\/docs\.gigaspaces\.com/g, "replace": '' }    
  ];
  // If is numeric, add meta tag to prevent search engines like google from indexing pages:
  if (!isNaN(sVersion)) {
      replaceTerms.push({ "find": '    <head>', "replace": '    <head>\n        <meta name="robots" content="noindex" />' });
  }
  processFilesWithExtension(dstFolder, ".html", replaceTerms);
  log("Processing static resources...");
  processStaticResources(fso.GetFolder(dstFolder.Path + "\\Resources\\Static"), sOutputPath);
  if (srcFolder.Name === "ie-resources") {
	log("Moving " + sDstPath + " to " + sOutputPath);
	fso.CopyFolder(sDstPath + "\\*", sOutputPath);
	fso.CopyFile(sDstPath + "\\*.*", sOutputPath + "\\");
	fso.DeleteFolder(sDstPath);
  }
}

function processStaticResources(srcPath, dstPath) {
  var fc;

  for (fc = new Enumerator(srcPath.SubFolders); !fc.atEnd(); fc.moveNext()) {
    processStaticResources(fc.item(), dstPath + "\\" + fc.item().name);
  }

  for (fc = new Enumerator(srcPath.files); !fc.atEnd(); fc.moveNext()) {
    var dstFile = dstPath + "\\"+ fc.item().name;
    if (fso.FileExists(dstFile)) {
      log("  Deleting "+ fc.item().Path);
      fc.item().Delete();
    } else {
      if (!fso.FolderExists(dstPath)) {
        fso.CreateFolder(dstPath)
      }
      log("  Moving " + fc.item().Path + " to " + dstFile);
      fc.item().Move(dstFile);
    }
  }
  
  log("  Deleting " + srcPath.Path);
  srcPath.Delete();
}

function processFilesWithExtension(folder, sExtension, replaceTerms) {
  var fc;

  for (fc = new Enumerator(folder.SubFolders); !fc.atEnd(); fc.moveNext()) {
    processFilesWithExtension(fc.item(), sExtension, replaceTerms);
  }

  for (fc = new Enumerator(folder.files); !fc.atEnd(); fc.moveNext()) {
    var fn = fc.item().name;
    if (fn.substr(fn.length - sExtension.length, sExtension.length) === sExtension) {
      processFile(fc.item().Path, replaceTerms);
    }
  }
}

function processFile(sFilename, replaceTerms) {
  var s = t = readText(sFilename);
  for (var i = 0; i < replaceTerms.length; i++) {
    s = s.replace(replaceTerms[i].find, replaceTerms[i].replace);
  }
  
  log("  " + (s !== t ? "Modified" : "Unchanged") + " " + sFilename);
  if (s !== t) {
	  writeText(sFilename, s)
  }
}

function readText(sFilename) {
  var ForReading = 1;
  var fh = fso.OpenTextFile(sFilename, ForReading);
  var s = fh.ReadAll();
  fh.Close();
  return s;
}

function writeText(sFilename, s) {
  var fh;

  if (fso.FileExists(sFilename)) {
    fh = fso.GetFile(sFilename);
    fh.Delete();
  }

  fh = fso.CreateTextFile(sFilename, true);
  fh.Write(s);
  fh.Close();
}

function log(sMessage, iLevel) {
  var l = iLevel || 1;
  if (logLevel >= l) {
    WScript.Echo(sMessage);
    lfh.WriteLine(sMessage);
  }
}
