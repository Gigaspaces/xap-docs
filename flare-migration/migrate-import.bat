@echo off
if not exist site-temp (
   echo Duplicating site-temp...
   mkdir site-temp
   xcopy /E ..\site site-temp
   ROBOCOPY /e "howto" "site-temp\content\xap\12.3\howto"
)