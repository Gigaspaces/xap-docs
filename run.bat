@echo off
call generate-navbar.bat
echo Starting Hugo...
hugo -s site-hugo server --watch
pause
