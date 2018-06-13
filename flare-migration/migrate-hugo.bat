@echo off
echo Starting Hugo...
pushd site-temp
hugo -d ..\output
popd
pause