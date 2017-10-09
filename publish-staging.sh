#!/bin/bash

set -e
export S3_BUCKET=$S3_BUCKET_STAGING
echo "Publishing staging to $S3_BUCKET (under construction)..."
