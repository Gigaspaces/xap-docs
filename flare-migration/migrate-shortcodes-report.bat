@echo off
call mvn --file jarvis package -DskipTests
set VERBOSE_REPORT=false
call java -jar jarvis/target/jarvis-1.0.jar analyze-shortcodes site-temp\content\xap\14.0 shortcodes-usage-14.0.csv
call java -jar jarvis/target/jarvis-1.0.jar analyze-shortcodes site-temp\content\sbp shortcodes-usage-sbp.csv