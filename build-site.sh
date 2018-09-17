set -x
set -e

./generate-navbar.sh .
#hugo/hugo -s ./site -d ../_site 
hugo_0.17/hugo -s ./site-hugo -d ../_site 
