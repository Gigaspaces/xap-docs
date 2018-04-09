@echo off
if not exist site-temp (
   echo Duplicating site-temp...
   mkdir site-temp
   xcopy /E ..\site site-temp
)

echo Overriding site-temp...
xcopy /Y /E site-overrides site-temp

pushd ..
call generate-navbar.bat flare-migration\site-temp
popd

echo Starting Hugo...
pushd site-temp
hugo -d ..\output
popd
pause