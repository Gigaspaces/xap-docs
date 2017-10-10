#!/bin/bash
set -e

export S3_ID=$STAGING_S3_ID
export S3_SECRET=$STAGING_S3_SECRET
export S3_BUCKET=docs-staging.gigaspaces.com
echo "Publishing staging to $S3_BUCKET..."
s3_website push

export S3_ID=$PROD_S3_ID
export S3_SECRET=$PROD_S3_SECRET
export S3_BUCKET=docs.gigaspaces.com-v2
echo "Publishing experimental production to $S3_BUCKET..."
s3_website push
