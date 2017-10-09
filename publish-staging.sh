#!/bin/bash

set -e
export S3_ID=$STAGING_S3_ID
export S3_SECRET=$STAGING_S3_SECRET
export S3_BUCKET=docs.gigaspaces.com-v2
echo "Publishing staging to $S3_BUCKET..."
s3_website push
