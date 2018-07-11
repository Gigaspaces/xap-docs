@echo off
setlocal
set fOut="output\xap\12.3"
echo Updating Flare content ...
cscript migrate-flare.js //nologo %fOut%
echo Copying files ...
ROBOCOPY /Mir %fOut% "..\..\xap-docs-flare\Content\xap\12.3"
echo Done