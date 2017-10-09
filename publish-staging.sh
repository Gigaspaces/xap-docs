#!/bin/bash

set -x
set -e
echo "Publishing to staging (under construction)..."
echo S3_BUCKET_STAGING=$S3_BUCKET_STAGING
echo S3_BUCKET_PRODUCTION=$S3_BUCKET_PRODUCTION
export S3_BUCKET=$S3_BUCKET_STAGING
echo S3_BUCKET=$S3_BUCKET
echo Done
