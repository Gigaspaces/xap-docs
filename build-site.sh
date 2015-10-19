set -x
set -e

hugo/hugo -s ./site -d ../_site 
./generate-navbar.sh .
