#!/bin/bash
set -xe

mvn -B package
mv target/mvc-tests-1.0-SNAPSHOT.jar ./app.jar

if [ -z "$TARGET_IMAGE_NAME" ]; then
	TARGET_IMAGE_NAME=target-image
fi
TARGET_IMAGE_TAG=`git rev-parse --short HEAD`
docker build -t $TARGET_IMAGE_NAME:$TARGET_IMAGE_TAG .
docker tag $TARGET_IMAGE_NAME:$TARGET_IMAGE_TAG $TARGET_IMAGE_NAME:latest

if [ "$PUSH_TO_ECR" == "true" ]; then
	TARGET_IMAGE_REG=`aws sts get-caller-identity --query 'Account' --output text`.dkr.ecr.${AWS_DEFAULT_REGION}.amazonaws.com
	aws ecr get-login-password | docker login --username AWS --password-stdin $TARGET_IMAGE_REG
	docker tag $TARGET_IMAGE_NAME:$TARGET_IMAGE_TAG $TARGET_IMAGE_REG/$TARGET_IMAGE_NAME:$TARGET_IMAGE_TAG
	docker tag $TARGET_IMAGE_NAME:latest $TARGET_IMAGE_REG/$TARGET_IMAGE_NAME:latest
	docker push $TARGET_IMAGE_REG/$TARGET_IMAGE_NAME:$TARGET_IMAGE_TAG
	docker push $TARGET_IMAGE_REG/$TARGET_IMAGE_NAME:latest
	
	echo [{\"name\": \"$TARGET_IMAGE_NAME\", \"imageUri\": \"$TARGET_IMAGE_REG/$TARGET_IMAGE_NAME:$TARGET_IMAGE_TAG\"}] > imagedefinitions.json
fi