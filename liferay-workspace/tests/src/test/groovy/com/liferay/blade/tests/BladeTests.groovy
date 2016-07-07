package com.liferay.blade.tests

import org.gradle.testkit.runner.GradleRunner
import static org.gradle.testkit.runner.TaskOutcome.*
import spock.lang.Specification
import de.undercouch.gradle.tasks.download.Download;
import java.io.FileNotFoundException;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Request.Builder;
import aQute.bnd.osgi.Jar;
import aQute.bnd.osgi.Processor;
import aQute.bnd.deployer.repository.FixedIndexedRepo;
import org.gradle.api.file.FileTree;
import org.gradle.api.GradleException;
import org.eclipse.core.runtime.Path;

class BladeTests extends Specification {

	def getLatestBladeCLIJar() {
		def repo = new FixedIndexedRepo()
    repo.setProperties([
			"name" : "index1",
			"locations" : getRepoURL() + "index.xml.gz",
			FixedIndexedRepo.PROP_CACHE : new File("build").absolutePath
		])
    repo.setReporter(new Processor())

		File[] files = repo.get( "com.liferay.blade.cli", "[1,2)" );
		File cliJar = files[0];

		return cliJar.canonicalPath
  }

	public void executeBlade(String... args) {
		def bladeclijar = getLatestBladeCLIJar()
		println bladeclijar

		"java -jar ${bladeclijar} args".execute()
	}

	def setup () {
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

			def sampleBundles = bladeSampleOutputFiles.files
			def sampleBundle = sampleBundles.name
			def bundleIDAllList = []
			def bundleIDStartList = []
			def errorList = []

		when:
			sampleBundles.each { sampleBundlefile ->
				def installBundleOutput = executeBlade(args.add('sh', 'install', "file:${sampleBundlefile}")).text()

				def bundleID = installBundleOutput.substring(installBundleOutput.length() - 3)

				if(installBundleOutput.contains("Failed")) {
					throw new GradleException(installBundleOutput)
				}

				bundleIDAllList.add(bundleID)

				def jarManifest = new aQute.bnd.osgi.Jar(sampleBundlefile).getmanifest().getMainAttributes();

				if (jarManifest.getValue('fragment-Host') == null) {
					bundleIDStartList.add(bundleID)
				}

				bundleIDAllList = bundleIDAllList.unique()
				bundleIDStartList = bundleIDStartList.unique()
			}

			bundleIDStartList.each { startBundleID ->
				def startOutput = executeBlade(args.add('sh', 'start', startBundleID)).text()

				if (startOutput.contains('Exception')) {
					errorList.add(startOutput)
				}
			}

		then:
			// check the list for error status

			//result.errorList.isEmpty == true
			assert(errorList.isEmpty());
			noExceptionThrown()

		cleanup:
			bundleIDAllList.each { bundleIDAll ->
				def uninstallOutput = executeBlade(args.add('sh', 'uninstall', bundleIDAll)).text()
			}
	}

	def teardown(){
		doLast {
			executeBlade(args.add('server', 'stop'))
		}
	}

}
