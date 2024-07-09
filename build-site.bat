@echo off
SETLOCAL EnableDelayedExpansion

if "%1"=="" (set DOCS_PUBLISH=none) else (set DOCS_PUBLISH=%1)
if NOT %DOCS_PUBLISH%==none (
  if %DOCS_PUBLISH%==staging set DOCS_BUCKET=docs-staging.gigaspaces.com
  if %DOCS_PUBLISH%==new-website set DOCS_BUCKET=docs-new-website.gigaspaces.com
  if %DOCS_PUBLISH%==production set DOCS_BUCKET=docs.gigaspaces.com-v2
  if not defined DOCS_BUCKET echo Aborted - unsupported destination [%DOCS_PUBLISH%] && exit /b 1
)

if %DOCS_PUBLISH%==production (
  set DOCS_BUILD_PROFILE=full
) else (
  if "%2"=="" (set DOCS_BUILD_PROFILE=default) else (set DOCS_BUILD_PROFILE=%2)
)
if %DOCS_PUBLISH%==production (
  set DOCS_BUILD_GOALS=all
) else (
  if "%3"=="" (set DOCS_BUILD_GOALS=flare) else (set DOCS_BUILD_GOALS=%3)
)
echo DOCS_BUILD_PROFILE=%DOCS_BUILD_PROFILE%, DOCS_BUILD_GOALS=%DOCS_BUILD_GOALS%, DOCS_PUBLISH=%DOCS_PUBLISH%

if %DOCS_BUILD_GOALS%==none (
  echo *** Skipping build - goals=none ***
  goto publish
)
echo *** Purging previous output ***
rmdir output /S /Q
mkdir output

call :build-%DOCS_BUILD_GOALS%
if %ERRORLEVEL% NEQ 0 exit /b %ERRORLEVEL%

if not defined DOCS_BUCKET echo Publishing skipped - destination not defined && goto end

if %DOCS_BUILD_PROFILE%==default echo Skipping noindex generation - profile=%DOCS_BUILD_PROFILE% && goto publish
echo *** Add noindex element to output ***
REM call jarvis.bat generate-noindex %~dp0

:publish
echo *** Publishing output ***
copy static\%DOCS_PUBLISH% output
if not defined DOCS_BUCKET (
  echo Publishing skipped - unsupported destination [%DOCS_PUBLISH%]
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
exit /b %ERRORLEVEL%

:build-flare
echo *** Building flare site ***
if not defined GS_FLARE_HOME set GS_FLARE_HOME=%ProgramW6432%\MadCap Software
if not defined GS_FLARE_BIN for /f "delims=" %%F in ('dir /b /s "%GS_FLARE_HOME%\\madbuild.exe"') do set GS_FLARE_BIN=%%F
if not defined GS_FLARE_BIN echo Aborting - Flare not found under %GS_FLARE_HOME% && exit /b 1
if not exist "%GS_FLARE_BIN%" echo Aborting - Flare not found at %GS_FLARE_BIN% && exit /b 1
echo GS_FLARE_BIN=%GS_FLARE_BIN%
"%GS_FLARE_BIN%" -project site-flare\GS-TechDocs.flprj -batch "InsightEdge-batch-%DOCS_BUILD_PROFILE%"
echo build-flare completed with errorlevel=%ERRORLEVEL%
rem Ignore errorlevel == 7 (missing link warning)
if %ERRORLEVEL%==7 exit /b 0
exit /b %ERRORLEVEL%

:end
