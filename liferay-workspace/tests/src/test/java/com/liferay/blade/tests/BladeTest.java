/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.liferay.blade.tests;

import aQute.bnd.osgi.Jar;

import aQute.lib.io.IO;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.Writer;

import java.nio.file.Files;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Request.Builder;

import org.gradle.testkit.runner.BuildTask;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertFalse;

/**
 * @author Lawrence Lee
 */
public class BladeTest {
	private File testDir;

	@BeforeClass
	public static void startServer() throws Exception {
		BladeCLI.execute(new File(".."), "server", "start", "-b");

		OkHttpClient client = new OkHttpClient();
		Request request = new Builder().url("http://localhost:8080").build();

		boolean pingSucceeded = false;

		while (!pingSucceeded) {
			try {
				client.newCall(request).execute();
				pingSucceeded = true;
				break;
			}
			catch( Exception e) {
			}
			Thread.sleep(100);
		}
	}

	@AfterClass
	public static void stopServer() throws Exception {
		BladeCLI.execute("server", "stop");
	}

	@Before
	public void setUp() throws Exception {
		testDir = Files.createTempDirectory("bladetest").toFile();
		testDir.deleteOnExit();
	}

	@After
	public void cleanUp() throws Exception {
		if (testDir.exists()) {
			IO.delete(testDir);
			assertFalse(testDir.exists());
		}
	}

	@Test
	public void verifyAllBladeSamples() throws Exception {
		List<String> bladeSampleOutputFiles = new ArrayList<String>();
		Map<String, String> bundleIDAllMap = new HashMap<String, String>();
		Map<String, String> bundleIDStartMap = new HashMap<String, String>();

		String[] sampleOutputFiles = System.getProperty("moduleOutputPaths").split(",");

		for (String file : sampleOutputFiles) {
			bladeSampleOutputFiles.add(file);
		}

		for (String sampleBundleFile : bladeSampleOutputFiles) {
			String installBundleOutput = BladeCLI.installBundle(new File(sampleBundleFile));

			String printFileName = new File(sampleBundleFile).getName();

			bundleIDAllMap.put(installBundleOutput, printFileName);

			try (Jar jar = new Jar(sampleBundleFile, sampleBundleFile)) {
				if (jar.getManifest().getMainAttributes().getValue("Fragment-Host") == null) {
					bundleIDStartMap.put(installBundleOutput, printFileName);
				}
			}
		}

		for (String startBundleIO : bundleIDStartMap.keySet()) {
			BladeCLI.startBundle(startBundleIO);
		}

		for (String allBundleID : bundleIDAllMap.keySet()) {
			BladeCLI.uninstallBundle(allBundleID);
		}
	}

	@Test
	public void verifyControlMenuEntryGradleTemplates() throws Exception {
		File projectPath = BladeCLI.createProject(testDir, "controlmenuentry", "helloworld");

		BuildTask buildtask = GradleRunnerUtil.executeGradleRunner(projectPath, "build");

		GradleRunnerUtil.verifyGradleRunnerOutput(buildtask);
		GradleRunnerUtil.verifyBuildOutput(projectPath.getPath(), "helloworld-1.0.0.jar");

		File buildOutput = new File(projectPath + "/build/libs/helloworld-1.0.0.jar");

		String bundleID = BladeCLI.installBundle(buildOutput);

		BladeCLI.startBundle(bundleID);

		BladeCLI.uninstallBundle(bundleID);
	}

	@Test
	public void verifyMVCPortletGradleTemplates () throws Exception {
		File projectPath = BladeCLI.createProject(testDir, "mvcportlet", "helloworld");

		BuildTask buildtask = GradleRunnerUtil.executeGradleRunner(projectPath, "build");

		GradleRunnerUtil.verifyGradleRunnerOutput(buildtask);
		GradleRunnerUtil.verifyBuildOutput(projectPath.getPath(), "helloworld-1.0.0.jar");

		File buildOutput = new File(projectPath + "/build/libs/helloworld-1.0.0.jar");

		String bundleID = BladeCLI.installBundle(buildOutput);

		BladeCLI.startBundle(bundleID);

		BladeCLI.uninstallBundle(bundleID);
	}

	@Test
	public void verifyPanelAppGradleTemplates () throws Exception {
		File projectPath = BladeCLI.createProject(testDir, "panelapp", "helloworld");

		BuildTask buildtask = GradleRunnerUtil.executeGradleRunner(projectPath, "build");

		GradleRunnerUtil.verifyGradleRunnerOutput(buildtask);
		GradleRunnerUtil.verifyBuildOutput(projectPath.getPath(), "helloworld-1.0.0.jar");

		File buildOutput = new File(projectPath + "/build/libs/helloworld-1.0.0.jar");

		String bundleID = BladeCLI.installBundle(buildOutput);

		BladeCLI.startBundle(bundleID);

		BladeCLI.uninstallBundle(bundleID);
	}

	@Test
	public void verifyPortletGradleTemplates () throws Exception {
		File projectPath = BladeCLI.createProject(testDir, "portlet", "helloworld");

		BuildTask buildtask = GradleRunnerUtil.executeGradleRunner(projectPath, "build");

		GradleRunnerUtil.verifyGradleRunnerOutput(buildtask);
		GradleRunnerUtil.verifyBuildOutput(projectPath.getPath(), "helloworld-1.0.0.jar");

		File buildOutput = new File(projectPath + "/build/libs/helloworld-1.0.0.jar");

		String bundleID = BladeCLI.installBundle(buildOutput);

		BladeCLI.startBundle(bundleID);

		BladeCLI.uninstallBundle(bundleID);
	}

	@Test
	public void verifyPortletProviderGradleTemplates () throws Exception {
		File projectPath = BladeCLI.createProject(testDir, "portletprovider", "helloworld");

		BuildTask buildtask = GradleRunnerUtil.executeGradleRunner(projectPath, "build");

		GradleRunnerUtil.verifyGradleRunnerOutput(buildtask);
		GradleRunnerUtil.verifyBuildOutput(projectPath.getPath(), "helloworld-1.0.0.jar");

		File buildOutput = new File(projectPath + "/build/libs/helloworld-1.0.0.jar");

		String bundleID = BladeCLI.installBundle(buildOutput);

		BladeCLI.startBundle(bundleID);

		BladeCLI.uninstallBundle(bundleID);
	}

	@Test
	public void verifyServiceGradleTemplate () throws Exception {
		BladeCLI.createProject(testDir, "service", "helloworld", "-s",
				"com.liferay.portal.kernel.events.LifecycleAction", "-c",
				"FooAction");

		File projectPath = new File(testDir + "/helloworld");

		File file = new File(projectPath + "/src/main/java/helloworld/FooAction.java");

		List<String> lines = new ArrayList<String>();
		String line = null;

		try(BufferedReader reader = new BufferedReader(new FileReader(file))) {
			while ((line = reader.readLine()) !=null) {
				lines.add(line);
				if (line.equals("import com.liferay.portal.kernel.events.LifecycleAction;")) {
					lines.add("import com.liferay.portal.kernel.events.LifecycleEvent;");
					lines.add("import com.liferay.portal.kernel.events.ActionException;");
				}

				if (line.equals("public class FooAction implements LifecycleAction {")) {
					String s = new StringBuilder()
					           .append("@Override\n")
					           .append("public void processLifecycleEvent(LifecycleEvent lifecycleEvent)\n")
					           .append("throws ActionException {\n")
					           .append("System.out.println(\"login.event.pre=\" + lifecycleEvent);\n")
					           .append("}\n")
					           .toString();
					lines.add(s);
				}
			}
		}

		try(Writer writer = new FileWriter(file)) {
			for(String string : lines){
				writer.write(string + "\n");
			}
		}

		BuildTask buildtask = GradleRunnerUtil.executeGradleRunner(projectPath, "build");

		GradleRunnerUtil.verifyGradleRunnerOutput(buildtask);
		GradleRunnerUtil.verifyBuildOutput(projectPath.getPath(), "helloworld-1.0.0.jar");

		File buildOutput = new File(projectPath + "/build/libs/helloworld-1.0.0.jar");

		String bundleID = BladeCLI.installBundle(buildOutput);

		BladeCLI.startBundle(bundleID);

		BladeCLI.uninstallBundle(bundleID);
	}

	@Test
	public void verifyServiceBuilderGradleTemplate () throws Exception {
		File projectPath = BladeCLI.createProject(testDir, "servicebuilder", "guestbook", "-p",
				"com.liferay.docs.guestbook");

		BuildTask buildService = GradleRunnerUtil.executeGradleRunner(projectPath, "buildService");
		GradleRunnerUtil.verifyGradleRunnerOutput(buildService);
		BuildTask buildtask = GradleRunnerUtil.executeGradleRunner(projectPath, "build");

		GradleRunnerUtil.verifyGradleRunnerOutput(buildtask);
		GradleRunnerUtil.verifyBuildOutput(projectPath + "/guestbook-api", "guestbook-api-1.0.0.jar");
		GradleRunnerUtil.verifyBuildOutput(projectPath + "/guestbook-service", "guestbook-service-1.0.0.jar");

		File buildApiOutput = new File(projectPath + "/guestbook-api/build/libs/guestbook-api-1.0.0.jar");
		File buildServiceOutput = new File(projectPath + "/guestbook-service/build/libs/guestbook-service-1.0.0.jar");

		String bundleIDApi = BladeCLI.installBundle(buildApiOutput);
		String bundleIDService = BladeCLI.installBundle(buildServiceOutput);

		BladeCLI.startBundle(bundleIDApi);
		BladeCLI.startBundle(bundleIDService);

		BladeCLI.uninstallBundle(bundleIDApi, bundleIDService);
	}

	@Test
	public void verifyServiceWrapperGradleTemplate () throws Exception {
		File projectPath = BladeCLI.createProject(testDir, "servicewrapper", "serviceoverride", "-s",
				"com.liferay.portal.kernel.service.UserLocalServiceWrapper");

		BuildTask buildtask = GradleRunnerUtil.executeGradleRunner(projectPath, "build");

		GradleRunnerUtil.verifyGradleRunnerOutput(buildtask);
		GradleRunnerUtil.verifyBuildOutput(projectPath.getPath(), "serviceoverride-1.0.0.jar");

		File buildOutput = new File(projectPath + "/build/libs/serviceoverride-1.0.0.jar");

		String bundleID = BladeCLI.installBundle(buildOutput);

		BladeCLI.startBundle(bundleID);

		BladeCLI.uninstallBundle(bundleID);
	}
}