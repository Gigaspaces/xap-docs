#!/bin/bash
set -e

#export S3_ID=$STAGING_S3_ID
#export S3_SECRET=$STAGING_S3_SECRET
#export S3_BUCKET=docs-staging.gigaspaces.com
#export S3_ENDPOINT=eu-west-1
#echo "Publishing staging to S3 bucket $S3_BUCKET at $S3_ENDPOINT..."
#s3_website push

export S3_ID=$PROD_S3_ID
export S3_SECRET=$PROD_S3_SECRET
export S3_BUCKET=docs.gigaspaces.com-v2
export S3_ENDPOINT=us-east-1
echo "Publishing experimental production to S3 bucket $S3_BUCKET at $S3_ENDPOINT..."
s3_website push
