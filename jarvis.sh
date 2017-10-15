#!/bin/bash

mvn --file jarvis package -DskipTests
javaw -cp jarvis/target/jarvis-1.0.jar com.gigaspaces.jarvis.ui.MainUI