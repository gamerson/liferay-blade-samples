#!/bin/bash
set -xe
repoDir=`pwd`
function buildAll() {
  cd "$repoDir"
  cd liferay-gradle
  ./gradlew checkSourceFormatting clean build -x testIntegration
  cd "$repoDir"
  cd maven
  mvn --fail-at-end clean package
  cd "$repoDir"
  cd liferay-workspace
  ./gradlew clean build -x testIntegration
  cd "$repoDir"
  ./gradlew bundlesTest warsTest diff
  cd liferay-workspace
  ./gradlew check -Pliferay.workspace.modules.dir=modules,tests $@
}
buildAll
