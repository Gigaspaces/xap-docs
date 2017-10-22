@echo off
call mvn --file jarvis package -DskipTests
start javaw -jar jarvis/target/jarvis-1.0.jar %*