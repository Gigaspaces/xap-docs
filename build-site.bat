@echo off
SETLOCAL EnableDelayedExpansion

echo *** Purging previous output ***
rmdir output /S /Q
mkdir output

echo *** Building hugo site ***
call run-hugo.bat -d ..\output


echo *** TEMP - migrating 12.3 output to flare content... ***
pushd flare-migration
rmdir site-temp /S /Q
rmdir output /S /Q
call migrate.bat
popd
echo *** Building flare site ***
"C:\Program Files\MadCap Software\MadCap Flare 14\Flare.app\madbuild.exe" -project site-flare\XAP-Import-Test-1.flprj -batch "InsightEdge-batch"
echo Skipped - under construction

echo *** Publishing output ***
if "%1"=="" (
  echo echo Publishing skipped - destination not defined
) else (
  if "%1"=="staging" set DOCS_BUCKET=docs-staging.gigaspaces.com
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