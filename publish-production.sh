#!/bin/bash
set -e

echo "Publishing production..."
export DOCS_TARGET=PROD
s3_website push