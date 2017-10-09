@echo off
call generate-navbar.bat
echo Starting Hugo...
pushd site
hugo server --watch
popd
pause
