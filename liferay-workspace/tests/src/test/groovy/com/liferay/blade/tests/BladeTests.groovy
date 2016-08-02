package com.liferay.blade.tests

import aQute.bnd.osgi.Jar;
import aQute.bnd.osgi.Processor;
import aQute.bnd.deployer.repository.FixedIndexedRepo;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Request.Builder;
import org.gradle.api.GradleException;
import org.gradle.testkit.runner.GradleRunner
import spock.lang.Specification
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import aQute.lib.io.IO;
import java.nio.file.Files;

import org.gradle.testkit.runner.BuildResult;
import org.gradle.testkit.runner.BuildTask;
import org.gradle.testkit.runner.TaskOutcome;


class BladeTests extends Specification {
	def bladeJarPath

	def executeBlade(String... args) {
		def bladeclijar = getLatestBladeCLIJar()
		def cmdline = "java -jar ${bladeclijar} ${args.join(' ')}"
		return cmdline.execute()
	}

	BuildTask executeGradleRunner (String projectPath, String... taskPath) {
		def projectDir = new File(projectPath)

		BuildResult buildResult = GradleRunner.create()
									.withProjectDir(projectDir)
									.withArguments(taskPath)
									.build()
		BuildTask buildtask = null;

		for (BuildTask task : buildResult.Tasks) {
			if (task.Path.endsWith(taskPath[taskPath.length - 1])) {
				buildtask = task;
				break;
			}
		}

		return buildtask;
	}

	def verifyGradleRunnerOutput (BuildTask buildtask) {
		assertNotNull(buildtask)

		assertEquals(buildtask.Outcome, TaskOutcome.SUCCESS)
	}

	def verifyBuildOutput (String projectPath, String filename) {
		def file = IO.getFile("${projectPath}/build/libs/${filename}")

		assertTrue(file.exists())
	}

	def getLatestBladeCLIJar() {
		if (bladeJarPath == null) {
			def repoPath = new File("build").absolutePath
			def repo = new FixedIndexedRepo()

			repo.setProperties([
				"name" : "index1",
				"locations" : "https://liferay-test-01.ci.cloudbees.com/job/liferay-blade-cli/lastSuccessfulBuild/artifact/build/generated/p2/index.xml.gz",
				"${FixedIndexedRepo.PROP_CACHE}" : repoPath
			])
			repo.setReporter(new Processor())

			File[] files = repo.get( "com.liferay.blade.cli", "[1,2)" );
			File cliJar = files[0];

			bladeJarPath = cliJar.canonicalPath
		}
		return bladeJarPath
	}

	def setupSpec () {
		println "Starting Server"

		executeBlade('server','start', '-b');

		OkHttpClient client = new OkHttpClient()
		Request request = new Builder().url("http://localhost:8080").build()

		boolean pingSucceeded = false

		while (!pingSucceeded) {
			try {
				client.newCall(request).execute()
				pingSucceeded = true
			}
			catch( Exception e) {
			}
		}
		println "Server Started"
	}

	/*def "verify all blade samples"() {
		given:
			def bladeSampleOutputFiles = []
			def bundleIDAllMap = [:]
			def bundleIDStartMap = [:]
			def errorList = []

			System.getProperty('moduleOutputPaths').split(",").each {
				bladeSampleOutputFiles.add(it)
			}

		when:
			bladeSampleOutputFiles.each { sampleBundleFile ->
				def installBundleOutput = executeBlade('sh', 'install', "file:${sampleBundleFile}").text

				def bundleID = installBundleOutput.substring(installBundleOutput.length() - 3)
				def printFileName = new File(sampleBundleFile).name
				printFileName = printFileName.split("-")[0];

				println "Installing ${printFileName}"

				if (installBundleOutput.contains("Failed")) {
					throw new GradleException(installBundleOutput)
				}

				bundleIDAllMap.put(bundleID, printFileName)

				if (new Jar(sampleBundleFile, sampleBundleFile).manifest.mainAttributes.getValue("Fragment-Host") == null) {
					bundleIDStartMap.put(bundleID, printFileName)
				}
			}

			bundleIDStartMap.keySet().each { startBundleID ->
				def startOutput = executeBlade('sh', 'start', startBundleID).text
				def startBundle = bundleIDStartMap[startBundleID]

				println "Starting ${startBundle}"

				if (startOutput.contains('Exception')) {
					errorList.add(startOutput)
				}
			}

		then:
			if (errorList.isEmpty()) {
				println errorList.each
			}
			noExceptionThrown()
			def listBundles = executeBlade('sh', 'lb').text
			println listBundles

		cleanup:
			bundleIDAllMap.keySet().each { allBundleID ->
				def uninstallOutput = executeBlade('sh', 'uninstall', allBundleID).text
				def uninstallBundle = bundleIDAllMap[allBundleID]

				println "Uninstalling ${uninstallBundle}"
			}
	}*/

	def "verify controlmenuentry gradle templates"() {
		given:
			def gradleTemplate = "controlmenuentry"
			def errorList = []

		when:
			def testDir = Files.createTempDirectory("samplestest").toFile();

			println "Testdir = ${testDir.absolutePath}"

			executeBlade('create', '-d', "${testDir.absolutePath}", '-t', "${gradleTemplate}", "helloworld");

			def projectPath = new File("${testDir}/helloworld")

			println "Project Path = ${projectPath}"

			def buildResult = GradleRunner.create()
           						.withProjectDir(projectPath)
            					.withArguments('build')
            					.build()

      def file = IO.getFile("${projectPath.absolutePath}/build/libs/helloworld-1.0.0.jar")

      assertTrue(file.exists())

			println "file.toURI() ${file.toURI()}"

      def installBundleOutput = executeBlade('sh', 'install', "${file.toURI()}").text

      def bundleID = installBundleOutput.substring(installBundleOutput.length() - 3)
      def printFileName = file.name
      printFileName = printFileName.split("-")[0];

      println "Installing ${printFileName}"

      if (installBundleOutput.contains("Failed")) {
				throw new GradleException(installBundleOutput)
      }

      def startOutput = executeBlade('sh', 'start', bundleID).text

      println "Starting ${printFileName}"

      if (startOutput.contains("Exception")) {
				errorList.add(startOutput)
      }

		then:
			if (errorList.isEmpty()) {
				println errorList.each
			}
			noExceptionThrown()
			def listBundles = executeBlade('sh', 'lb').text
			println listBundles

		cleanup:
			def uninstallOutput = executeBlade('sh', 'uninstall', bundleID).text

			println "Uninstalling ${printFileName}"

			/*if (testDir.exists()) {
				IO.delete(testDir);
				assertFalse(testDir.exists());
			}*/
	}

	/*def "mvcportlet gradle template"() {
		given:
			def gradleTemplate = "mvcportlet"
			def errorList = []


		when:
			def testDir = Files.createTempDirectory("samplestest").toFile();

			if (testDir.exists()) {
				IO.delete(testDir);
				assertFalse(testDir.exists());
			}

			testDir.mkdirs()

			executeBlade('create', '-d', "${testDir.absolutePath}", '-t', "${gradleTemplate}", "helloworld");

			println "Testdir = ${testDir.absolutePath}"

			def projectPath = new File("${testDir}/helloworld")

			println "Project Path = ${projectPath}"

			BuildTask buildtask = executeGradleRunner(projectPath, "build")

			verifyGradleRunnerOutput(buildtask)

			verifyBuildOutput(projectPath, "helloworld-1.0.0.jar")

			def file = IO.getFile("${projectPath}/build/libs/helloworld-1.0.0.jar")

            def installBundleOutput = executeBlade('sh', 'install', "file:${file}").text

            def bundleID = installBundleOutput.substring(installBundleOutput.length() - 3)
            def printFileName = new File(file).name
            printFileName = printFileName.split("-")[0];

            println "Installing ${printFileName}"

            if (installBundleOutput.contains("Failed")) {
            	throw new GradleException(installBundleOutput)
            }

            def startOutput = executeBlade('sh', 'start', bundleID).text

            println "Starting ${printFileName}"

            if (startOutput.contains("Exception")) {
            	errorList.add(startOutput)
            }

		then:
			if (errorList.isEmpty()) {
				println errorList.each
			}
			noExceptionThrown()
			def listBundles = executeBlade('sh', 'lb').text
			println listBundles

		cleanup:
			def uninstallOutput = executeBlade('sh', 'uninstall', bundleID).text

			println "Uninstalling ${printFileName}"

			if (testDir.exists()) {
				IO.delete(testDir);
				assertFalse(testDir.exists());
			}
	}*/

	def cleanupSpec(){
		executeBlade('server', 'stop')
		println "Stopping Server"
	}

}