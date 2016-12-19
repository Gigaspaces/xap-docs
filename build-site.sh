set -x
set -e

./generate-navbar.sh .
hugo/hugo -s ./site -d ../_site 
cp -R site/public/xap100net/ site/public/xap/10.0/dev/dotnet/
