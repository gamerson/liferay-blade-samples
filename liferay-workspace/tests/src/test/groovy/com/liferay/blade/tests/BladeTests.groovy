package com.liferay.blade.tests

import org.gradle.testkit.runner.GradleRunner
import static org.gradle.testkit.runner.TaskOutcome.*
import spock.lang.Specification
import de.undercouch.gradle.tasks.download.Download;
import java.util.jar.JarInputStream;
import java.io.FileNotFoundException;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Request.Builder;

class BladeTests extends Specification {
	def setup () {
		def initBundle = GradleRunner.create()
			.withProjectDir(new File(projectDir))
			.withArguments('initBundle')
			.build()

		def bladeclijar = 'com.liferay.blade.cli_1.0.1.201606142156.jar'
		def url = 'http://releases.liferay.com/tools/blade-cli/1.0.1.201606142156/plugins/${bladeclijar}'
		def file = new File("${bladeclijar}")

		if (!file.isFile()) {
			file.newOutputStream()
			file << URL(url).openStream()
			file.close()
		}

		if (!file.isFile()) {
			throw new GradleException(new FileNotFoundException());
		}

		public void executeBlade(arg1='', arg2='', arg3='', arg4='') {
			"java -jar build/${bladeclijar} ${arg1} ${arg2} ${arg3} ${arg4}".execute()
		}

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
	}

	def "verify all blade samples"() {
		given:
			FileTree bladeSampleOutputFiles = fileTree(dir: 'modules/', include: '**/libs/*.jar')

			def outputFilesTest = GradleRunner.create()
				.withProjectDir(new File(../projectDir))
				.withArguments('outputFilesTest')
				.build()

			def sampleBundles = bladeSampleOutputFiles.files
			def sampleBundle = sampleBundles.name
			def bundleIDAllList = []
			def bundleIDStartList = []
			def errorList = []

		when:
			sampleBundles.each { sampleBundlefile ->
				def installBundleOutput = executeBlade('sh', 'install', "file:${sampleBundlefile}").text()

				def bundleID = installBundleOutput.substring(installBundleOutput.length() - 3)

				if(installBundleOutput.contains("Failed") {
					throw new GradleException(installBundleOutput)
				}

				bundleIDAllList.add(bundleID)

				public void checkManifest(arg1='', arg2='', arg3='') {
					def jarInputStream = new JarInputStream(new FileInputStream("${arg1}"))

					jarInputStream.withCloseable {
						if (jarInputStream.manifest.getMainAttributes().getValue("${arg2}") == "${arg3}" ) {
							return true;
						}
						else {
							return false;
						}
					}
				}

				if (checkManifest(sampleBundlefile, 'fragment-Host', 'null')) {
					bundleIDStartList.add(bundleID)
				}

				bundleIDAllList = bundleIDAllList.unique()
				bundleIDStartList = bundleIDStartList.unique()
			}

			bundleIDStartList.each { startBundleID ->
				def startOutput = executeBlade('sh', 'start', startBundleID).text()

				if (startOutput.contains('Exception')) {
					errorList.add(startOutput)
				}
			}

		then:
			// check the list for error status

			//result.errorList.isEmpty == true
			assert(errorList.isEmpty());

			result.task(":build").outcome == SUCCESS

		cleanup:
			bundleIDAllList.each { bundleIDAll ->
				def uninstallOutput = executeBlade('sh', 'uninstall', bundleIDAll).text()
			}

			executeBlade('server', 'stop')

	}

	def "verify new blade template projects"() {

	}

}