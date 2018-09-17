@echo off
echo *** Purging previous output ***
rmdir output /S /Q
mkdir output
echo *** Building hugo site ***
call build-site-hugo.bat
echo *** Building flare site ***
call build-site-flare.bat
echo *** Site build completed ***
