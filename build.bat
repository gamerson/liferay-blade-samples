set repoDir=%cd%

call cd bndtools
call gradlew.bat clean build
call cd %repoDir%
call cd gradle
call gradlew.bat clean build
call cd %repoDir%
call cd liferay-gradle
call gradlew.bat clean build
call cd %repoDir%
call cd maven
call mvn --fail-at-end clean package
call cd %repoDir%
call cd liferay-workspace
call gradlew.bat clean build -Pliferay.workspace.bundle.url=http://mirrors/releases.liferay.com/portal/7.0.2-ga3/liferay-ce-portal-tomcat-7.0-ga3-20160804222206210.zip -Phttp.proxyHost=squid.lax.liferay.com -Phttp.proxyPort=3128
call cd %repoDir%
call gradlew.bat outputFilesTest diff
