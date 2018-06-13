@echo off
call mvn --file jarvis package -DskipTests
call java -jar jarvis/target/jarvis-1.0.jar %~dp0 site-temp