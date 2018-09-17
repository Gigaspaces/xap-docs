#!/bin/bash

set -x
set -e
./generate-navbar.sh . 
echo Starting Hugo...
hugo -s site-hugo server --watch -v
