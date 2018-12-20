@echo off
setlocal
REM fOut is the path to the content to migrate
set fOut=".\output\xap\14.0"
echo Updating Flare content ...
cscript migrate-flare.js //nologo %fOut%
echo Copying files ...
REM second parameter is the path where to store it in Flare
ROBOCOPY /e %fOut% "..\site-flare\Content"
echo Done