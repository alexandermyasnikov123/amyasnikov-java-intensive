#!/bin/sh

ROOT_DIR="$(git rev-parse --show-toplevel 2>/dev/null)"
THIS_DIR="$ROOT_DIR/java-basic-core-jvm"

echo "Execution of the clean jar is started..."
cd "$THIS_DIR" && "$ROOT_DIR"/./gradlew clean jar

echo "Running the archived result at $THIS_DIR"
java  -jar build/libs/app.jar -Xms256m -Xmx1024m -Xss1m -XX:+UseG1GC -XX:+HeapDumpOnOutOfMemoryError
