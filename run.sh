#!/bin/bash

./generate-navbar.sh .
echo Starting Hugo...
pushd site
hugo server --watch
popd
