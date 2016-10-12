#!/bin/bash
repoDir=`pwd`
function buildAll() {
  cd "$repoDir"
  cd bndtools
  ./gradlew clean build
  cd "$repoDir"
  cd gradle
  ./gradlew clean build
  cd "$repoDir"
  cd liferay-gradle
  ./gradlew clean build
  cd "$repoDir"
  cd maven
  mvn --fail-at-end clean package
  cd "$repoDir"
  cd liferay-workspace
  ./gradlew clean build -Pliferay.workspace.bundle.url=http://mirrors/releases.liferay.com/portal/7.0.2-ga3/liferay-ce-portal-tomcat-7.0-ga3-20160804222206210.zip -Phttp.proxyHost=squid.lax.liferay.com -Phttp.proxyPort=3128
  cd "$repoDir"
  ./gradlew outputFilesTest diff
}
buildAll
