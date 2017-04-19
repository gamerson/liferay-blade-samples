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
import aQute.bnd.osgi.Jar;

class BladeTests extends Specification {
	public void executeBlade(String[] args) {
		"java -jar build/${bladeclijar} args".execute()
	}

	def setup () {
	    private static String getLatestRemoteBladeCLIJar() throws BladeCLIException
	    {
	        _settingsDir.mkdirs();
	        repoCache.mkdirs();

	        Processor reporter = new Processor();
	        FixedIndexedRepo repo = new FixedIndexedRepo();
	        Map<String, String> props = new HashMap<String, String>();
	        props.put( "name", "index1" );
	        props.put( "locations", getRepoURL()+"index.xml.gz" );
	        props.put( FixedIndexedRepo.PROP_CACHE, repoCache.getAbsolutePath() );

	        repo.setProperties( props );
	        repo.setReporter( reporter );

	        try
	        {
	            File[] files = repo.get( "com.liferay.blade.cli", "[1,2)" );

	            File cliJar = files[0];

	            cachedBladeCLIPath = new Path( cliJar.getCanonicalPath() );

	            return cliJar.getName();
	        }
	        catch( Exception e )
	        {
	            throw new BladeCLIException( "Could not get blade cli jar from repository." );
	        }
	    }

		def bladeclijar = cliJar.getName();

		executeBlade(new String[]{'server','start', '-b'});

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
				def installBundleOutput = executeBlade(new String[]{'sh', 'install', "file:${sampleBundlefile}"}).text()

				def bundleID = installBundleOutput.substring(installBundleOutput.length() - 3)

				if(installBundleOutput.contains("Failed") {
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
				def startOutput = executeBlade(new String[]{'sh', 'start', startBundleID}).text()

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
				def uninstallOutput = executeBlade(new String[]{'sh', 'uninstall', bundleIDAll}).text()
			}
	}

	def "verify new blade template projects"() {

	}
	def teardown(){
		doLast {
			executeBlade(new String[]{'server', 'stop'})
		}
	}

}