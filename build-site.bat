@echo off
SETLOCAL EnableDelayedExpansion

if "%1"=="" (set DOCS_PUBLISH=none) else (set DOCS_PUBLISH=%1)
if NOT %DOCS_PUBLISH%==none (
  if %DOCS_PUBLISH%==staging set DOCS_BUCKET=docs-staging.gigaspaces.com
  if %DOCS_PUBLISH%==new-website set DOCS_BUCKET=docs-new-website.gigaspaces.com
  if %DOCS_PUBLISH%==production set DOCS_BUCKET=docs.gigaspaces.com-v2
  if not defined DOCS_BUCKET echo echo Aborted - unsupported destination [%DOCS_PUBLISH%] && goto end
)

if %DOCS_PUBLISH%==production (
  set DOCS_BUILD_GOALS=all
) else (
  if "%2"=="" (set DOCS_BUILD_GOALS=flare) else (set DOCS_BUILD_GOALS=%2)
)
echo DOCS_BUILD_GOALS=%DOCS_BUILD_GOALS%, DOCS_PUBLISH=%DOCS_PUBLISH%

echo *** Purging previous output ***
rmdir output /S /Q
mkdir output

call :build-%DOCS_BUILD_GOALS%

if not defined DOCS_BUCKET echo Publishing skipped - destination not defined && goto end

echo *** Add noindex element to output ***
call jarvis.bat generate-noindex %~dp0

echo *** Publishing output ***
copy static\%DOCS_PUBLISH% output
if not defined DOCS_BUCKET (
  echo echo Publishing skipped - unsupported destination [%DOCS_PUBLISH%]
) else (
  echo Publishing site to %DOCS_PUBLISH% at !DOCS_BUCKET!...
  set AWS_PROFILE=%DOCS_PUBLISH%
  aws s3 sync --delete --storage-class=REDUCED_REDUNDANCY output s3://!DOCS_BUCKET!
)
echo *** Site build completed ***
goto end

:build-all
call :build-hugo
call :build-flare
exit /b

:build-hugo
echo *** Building hugo site ***
call run-hugo.bat -d ..\output
exit /b

:build-flare
echo *** Building flare site ***
"C:\Program Files\MadCap Software\MadCap Flare 14\Flare.app\madbuild.exe" -project site-flare\XAP-Import-Test-1.flprj -batch "InsightEdge-batch"
exit /b

:end
