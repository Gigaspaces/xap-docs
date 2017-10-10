#!/bin/bash
set -e

#echo "Publishing staging..."
#export DOCS_TARGET=STAGING
#s3_website push

echo "Publishing experimental production..."
export DOCS_TARGET=PROD_EXP
s3_website push
