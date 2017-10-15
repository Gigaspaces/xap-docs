@echo off
if not exist jarvis/target/jarvis-1.0.jar call mvn --file jarvis package -DskipTests
start javaw -cp jarvis/target/jarvis-1.0.jar com.gigaspaces.jarvis.ui.MainUI