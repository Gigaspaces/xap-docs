@echo off
setlocal
set fOut=".\output\xap\14.0"
echo Updating Flare content ...
cscript migrate-flare.js //nologo %fOut%
echo Copying files ...
ROBOCOPY /e %fOut% "..\site-flare\Content"
echo Done