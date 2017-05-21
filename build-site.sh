set -x
set -e

./generate-navbar.sh .
#hugo/hugo -s ./site -d ../_site 
hugo_0.17/hugo -s ./site -d ../_site 

# 10.1 redirects
mkdir -p _site/xap/10.1/dev/java & cp -R _site/xap101/* _site/xap/10.1/dev/java
mkdir -p _site/xap/10.1/dev/dotnet & cp -R _site/xap101net/* _site/xap/10.1/dev/dotnet
mkdir -p _site/xap/10.1/tutorial/java & cp -R _site/xap101tut/* _site/xap/10.1/tutorial/java
mkdir -p _site/xap/10.1/tutorial/dotnet & cp -R _site/xap101tutnet/* _site/xap/10.1/tutorial/dotnet
mkdir -p _site/xap/10.1/admin & cp -R _site/xap101adm/* _site/xap/10.1/admin
mkdir -p _site/xap/10.1/security & cp -R _site/xap101sec/* _site/xap/10.1/security
