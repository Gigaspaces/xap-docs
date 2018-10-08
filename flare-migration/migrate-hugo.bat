@echo off
echo Starting Hugo...
pushd site-temp
..\..\bin\hugo-0.17\hugo.exe -d ..\output
popd