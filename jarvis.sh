#!/bin/bash
mvn --file jarvis package -DskipTests
java -jar jarvis/target/jarvis-1.0.jar $*