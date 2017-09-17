@echo off
call generate-navbar.bat
echo Starting Hugo...
pushd site
c:\tools\hugo-0.17\hugo server --watch
popd
