@echo off
call mvn --file jarvis package -DskipTests
call java -jar jarvis/target/jarvis-1.0.jar analyze-shortcodes site-temp\content\xap\12.3 shortcodes-usage-12.3.csv
call java -jar jarvis/target/jarvis-1.0.jar analyze-shortcodes site-temp\content\sbp shortcodes-usage-sbp.csv