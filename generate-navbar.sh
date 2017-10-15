set -x
set -e
echo Compiling navbar generator
rm -rf ./target
mkdir ./target
javac src/xapdoc/parser/* -d ./target

echo Running navbar generator
java -cp ./target xapdoc.parser.MenuTree $1
echo testing maven
java --version
mvn --version
