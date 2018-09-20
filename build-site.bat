@echo off
echo *** Purging previous output ***
rmdir output /S /Q
mkdir output
echo *** Building hugo site ***
call build-site-hugo.bat
echo *** Building flare site ***
call build-site-flare.bat
if defined Destination (
  if %Destination%==staging (set DOCS_BUCKET=docs-staging.gigaspaces.com) else (set DOCS_BUCKET=docs.gigaspaces.com-v2)
  echo Publishing site to !Destination! (!DOCS_BUCKET!)
  set AWS_PROFILE=!Destination!
  aws s3 sync --delete --storage-class=REDUCED_REDUNDANCY output s3://!DOCS_BUCKET!
)
echo *** Site build completed ***
