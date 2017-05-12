#!/bin/bash
set -xe
repoDir=`pwd`
function buildAll() {
  cd "$repoDir"
  cd liferay-gradle
  ./gradlew checkSourceFormatting clean build
  cd "$repoDir"
  cd maven
  mvn --fail-at-end clean package
  cd "$repoDir"
  cd liferay-workspace
  ./gradlew clean build -x testIntegration
  cd "$repoDir"
  ./gradlew bundlesTest warsTest diff
  cd liferay-workspace
  ./gradlew testIntegration -Pliferay.workspace.modules.dir=modules,tests $@
  cd tests
  ../gradlew testFunctional -Pliferay.workspace.modules.dir=modules,tests $@
}
buildAll
