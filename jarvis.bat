@echo off
call mvn --file jarvis package -DskipTests
IF [%1]==[] (
   start javaw -jar jarvis/target/jarvis-1.0.jar
) ELSE ( 
   java -jar jarvis/target/jarvis-1.0.jar %*
)
