#!/bin/bash
set -e

mvn -B package
mv target/mvc-tests-1.0-SNAPSHOT.jar ./app.jar

if [ -z "$TARGET_IMAGE" ]; then
	TARGET_IMAGE=spring-boot-testing-strategies
fi
docker build -t $TARGET_IMAGE .