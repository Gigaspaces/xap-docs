#!/bin/bash

set -x
set -e
./generate-navbar.sh . 
echo Starting Hugo...
pushd site
hugo server --watch -v
popd
