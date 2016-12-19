set -x
set -e

./generate-navbar.sh .
hugo/hugo -s ./site -d ../_site 
ls ../_site
cp -R ../_site/xap100net/ ../_site/xap/10.0/dev/dotnet/
