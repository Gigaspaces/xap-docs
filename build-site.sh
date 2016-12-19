set -x
set -e

./generate-navbar.sh .
hugo/hugo -s ./site -d ../_site 
# mkdir -p _site/xap/10.0/dev/dotnet & cp -R _site/xap100net/* _site/xap/10.0/dev/dotnet
