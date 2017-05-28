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

# 10.2 redirects
mkdir -p _site/xap/10.2
mv _site/xap102 _site/xap/10.2/dev-java
mv _site/xap102net _site/xap/10.2/dev-dotnet
mv _site/xap102tut _site/xap/10.2/tut-java
mv _site/xap102nettut _site/xap/10.2/tut-dotnet
mv _site/xap102adm _site/xap/10.2/admin
mv _site/xap102sec _site/xap/10.2/security

# 11.0 redirects
mkdir -p _site/xap/11.0
mv _site/xap110 _site/xap/11.0/dev-java
mv _site/xap110net _site/xap/11.0/dev-dotnet
mv _site/xap110tut _site/xap/11.0/tut-java
mv _site/xap110nettut _site/xap/11.0/tut-dotnet
mv _site/xap110adm _site/xap/11.0/admin
mv _site/xap110sec _site/xap/11.0/security
