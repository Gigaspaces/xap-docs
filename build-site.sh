set -x
set -e

./generate-navbar.sh .
#hugo/hugo -s ./site -d ../_site 
hugo_0.17/hugo -s ./site -d ../_site 

# 10.1 redirects
mkdir -p _site/xap/10.1
mv _site/xap101 _site/xap/10.1/dev-java
mv _site/xap101net _site/xap/10.1/dev-dotnet
mv _site/xap101tut _site/xap/10.1/tut-java
mv _site/xap101nettut _site/xap/10.1/tut-dotnet
mv _site/xap101adm _site/xap/10.1/admin
mv _site/xap101sec _site/xap/10.1/security
