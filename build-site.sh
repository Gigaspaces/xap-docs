set -x
set -e

./generate-navbar.sh .
#hugo/hugo -s ./site -d ../_site 
hugo_0.17/hugo -s ./site -d ../_site 

# 10.0 redirects
mkdir -p _site/xap/10.0
mv _site/xap100 _site/xap/10.0/dev-java
mv _site/xap100net _site/xap/10.0/dev-dotnet
mv _site/xap100adm _site/xap/10.0/admin
mv _site/xap100sec _site/xap/10.0/security
