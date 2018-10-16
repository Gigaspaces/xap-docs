@echo off
cls
call migrate-import.bat
call migrate-override.bat
call migrate-sidenav.bat
call migrate-hugo.bat
call migrate-flare.bat