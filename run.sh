#!/bin/bash

set -x
set -e
./generate-navbar.sh . 
echo Starting Hugo...
pushd site
/home/yaeln-pcu/IdeaProjects/xap-docs/hugo_0.17/hugo server --watch -v
popd
