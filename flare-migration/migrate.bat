@echo off
cls
call migrate-import.bat
call migrate-override.bat
call migrate-sidenav.bat
call migrate-hugo.bat
REM call migrate-flare.bat xap\12.3
call migrate-flare.bat sbp sbp