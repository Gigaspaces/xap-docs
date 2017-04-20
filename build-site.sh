set -x
set -e

./generate-navbar.sh .
#hugo/hugo -s ./site -d ../_site 
hugo_0.17/hugo -s ./site -d ../_site 
mkdir -p _site/xap/10.1/dev/dotnet & cp -R _site/xap101net/* _site/xap/10.1/dev/dotnet
mkdir -p _site/xap/10.1/net & cp -R _site/xap101net/* _site/xap/10.1/net
