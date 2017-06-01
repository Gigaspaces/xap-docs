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

# 12.0 redirects
mkdir -p _site/xap/12.0
mv _site/xap120 _site/xap/12.0/dev-java
mv _site/xap120net _site/xap/12.0/dev-dotnet
mv _site/xap120tut _site/xap/12.0/tut-java
mv _site/xap120nettut _site/xap/12.0/tut-dotnet
mv _site/xap120adm _site/xap/12.0/admin
mv _site/xap120sec _site/xap/12.0/security

# 12.1 redirects
mkdir -p _site/xap/12.1
mv _site/xap121 _site/xap/12.1/dev-java
mv _site/xap121net _site/xap/12.1/dev-dotnet
mv _site/xap121tut _site/xap/12.1/tut-java
mv _site/xap121nettut _site/xap/12.1/tut-dotnet
mv _site/xap121adm _site/xap/12.1/admin
mv _site/xap121sec _site/xap/12.1/security
