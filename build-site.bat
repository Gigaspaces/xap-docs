@echo off
SETLOCAL EnableDelayedExpansion

echo *** Purging previous output ***
rmdir output /S /Q
mkdir output

echo *** Building hugo site ***
call run-hugo.bat -d ..\output

echo *** Building flare site ***
"C:\Program Files\MadCap Software\MadCap Flare 14\Flare.app\madbuild.exe" -project site-flare\XAP-Import-Test-1.flprj -batch "InsightEdge-batch"

echo *** Add noindex element to output ***
call jarvis.bat generate-noindex %~dp0

echo *** Publishing output ***
if "%1"=="" (
  echo echo Publishing skipped - destination not defined
) else (
  copy static\%1 output
  if "%1"=="staging" set DOCS_BUCKET=docs-staging.gigaspaces.com
  if "%1"=="new-website" set DOCS_BUCKET=docs-new-website.gigaspaces.com
  if "%1"=="production" set DOCS_BUCKET=docs.gigaspaces.com-v2
  if not defined DOCS_BUCKET (
    echo echo Publishing skipped - unsupported destination [%1]
  ) else (
    echo Publishing site to %1 at !DOCS_BUCKET!...
    set AWS_PROFILE=%1
    aws s3 sync --delete --storage-class=REDUCED_REDUNDANCY output s3://!DOCS_BUCKET!
  )
)
echo *** Site build completed ***