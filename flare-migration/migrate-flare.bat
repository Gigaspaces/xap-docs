@echo off
setlocal
echo Updating Flare content for %1 ...
cscript migrate-flare.js //nologo output\%1
echo Copying files ...
REM second parameter is the path where to store it in Flare
ROBOCOPY /e output\%1 ..\site-flare\Content\%2
echo Done