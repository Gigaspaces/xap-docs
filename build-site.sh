set -x
set -e

./generate-navbar.sh .
hugo/hugo -s ./site -d ../_site 

