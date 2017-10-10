#!/bin/bash
set -e

export S3_ID=$S3_ACCESS_KEY_ID
export S3_SECRET=$S3_SECRET_KEY
export S3_BUCKET=docs.gigaspaces.com
export S3_ENDPOINT=us-east-1
echo "Publishing production to S3 bucket $S3_BUCKET at $S3_ENDPOINT..."
s3_website push