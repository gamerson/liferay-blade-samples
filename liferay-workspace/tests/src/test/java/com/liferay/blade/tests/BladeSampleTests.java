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

import static org.junit.Assert.assertFalse;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.Writer;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import org.gradle.testkit.runner.BuildTask;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import aQute.lib.io.IO;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Request.Builder;

/**
 * @author Lawrence Lee
 */
public class BladeSampleTests {
	@BeforeClass
	public static void startServer() throws Exception {
		System.out.println("Starting Server");

		BladeCommandUtil.executeBlade("server", "start", "-b");

		OkHttpClient client = new OkHttpClient();
		Request request = new Builder().url("http://localhost:8080").build();

		boolean pingSucceeded = false;

		while (!pingSucceeded) {
			try {
				client.newCall(request).execute();
				pingSucceeded = true;
			}
			catch( Exception e) {
			}
		}
		System.out.println("Server Started");
	}

	@AfterClass
	public static void stopServer() throws Exception {
		BladeCommandUtil.executeBlade("server", "stop");
		System.out.println("Stopping Server");
	}

	@Before
	public void setUp() throws Exception {
		testDir = Files.createTempDirectory("samplestest").toFile();

		if (testDir.exists()) {
			IO.delete(testDir);
			assertFalse(testDir.exists());
		}
	}

	@After
	public void uninstallBundle () throws Exception {
		BladeCommandUtil.executeBlade("sh", "uninstall", bundleID);
	}

	public void cleanUp() throws Exception {
		if (testDir.exists()) {
			IO.delete(testDir);
			assertFalse(testDir.exists());
		}
	}

	@Test
	public void verifyControlMenuEntryGradleTemplates () throws Exception {
		File projectPath = BladeCommandUtil.createBladeBundle(testDir, "controlmenuentry", "helloworld");

		BuildTask buildtask = GradleRunnerUtil.executeGradleRunner(projectPath, "build");

		GradleRunnerUtil.verifyGradleRunnerOutput(buildtask);
		GradleRunnerUtil.verifyBuildOutput(projectPath.getPath(), "helloworld-1.0.0.jar");

		File buildOutput = new File(projectPath + "/build/libs/helloworld-1.0.0.jar");

		bundleID = BladeCommandUtil.installBundle(buildOutput);

		BladeCommandUtil.startBundle(bundleID);

		String listBundleOutput = BladeCommandUtil.executeBlade("sh", "lb", "-s", "helloworld");

		System.out.println(listBundleOutput);
	}

	@Test
	public void verifyMVCPortletGradleTemplates () throws Exception {
		File projectPath = BladeCommandUtil.createBladeBundle(testDir, "mvcportlet", "helloworld");

		BuildTask buildtask = GradleRunnerUtil.executeGradleRunner(projectPath, "build");

		GradleRunnerUtil.verifyGradleRunnerOutput(buildtask);
		GradleRunnerUtil.verifyBuildOutput(projectPath.getPath(), "helloworld-1.0.0.jar");

		File buildOutput = new File(projectPath + "/build/libs/helloworld-1.0.0.jar");

		bundleID = BladeCommandUtil.installBundle(buildOutput);

		BladeCommandUtil.startBundle(bundleID);

		String listBundleOutput = BladeCommandUtil.executeBlade("sh", "lb", "-s", "helloworld");

		System.out.println(listBundleOutput);
	}

	@Test
	public void verifyPanelAppGradleTemplates () throws Exception {
		File projectPath = BladeCommandUtil.createBladeBundle(testDir, "panelapp", "helloworld");

		BuildTask buildtask = GradleRunnerUtil.executeGradleRunner(projectPath, "build");

		GradleRunnerUtil.verifyGradleRunnerOutput(buildtask);
		GradleRunnerUtil.verifyBuildOutput(projectPath.getPath(), "helloworld-1.0.0.jar");

		File buildOutput = new File(projectPath + "/build/libs/helloworld-1.0.0.jar");

		bundleID = BladeCommandUtil.installBundle(buildOutput);

		BladeCommandUtil.startBundle(bundleID);

		String listBundleOutput = BladeCommandUtil.executeBlade("sh", "lb", "-s", "helloworld");

		System.out.println(listBundleOutput);
	}

	@Test
	public void verifyPortletGradleTemplates () throws Exception {
		File projectPath = BladeCommandUtil.createBladeBundle(testDir, "portlet", "helloworld");

		BuildTask buildtask = GradleRunnerUtil.executeGradleRunner(projectPath, "build");

		GradleRunnerUtil.verifyGradleRunnerOutput(buildtask);
		GradleRunnerUtil.verifyBuildOutput(projectPath.getPath(), "helloworld-1.0.0.jar");

		File buildOutput = new File(projectPath + "/build/libs/helloworld-1.0.0.jar");

		bundleID = BladeCommandUtil.installBundle(buildOutput);

		BladeCommandUtil.startBundle(bundleID);

		String listBundleOutput = BladeCommandUtil.executeBlade("sh", "lb", "-s", "helloworld");

		System.out.println(listBundleOutput);
	}

	@Test
	public void verifyPortletProviderGradleTemplates () throws Exception {
		File projectPath = BladeCommandUtil.createBladeBundle(testDir, "portletprovider", "helloworld");

		BuildTask buildtask = GradleRunnerUtil.executeGradleRunner(projectPath, "build");

		GradleRunnerUtil.verifyGradleRunnerOutput(buildtask);
		GradleRunnerUtil.verifyBuildOutput(projectPath.getPath(), "helloworld-1.0.0.jar");

		File buildOutput = new File(projectPath + "/build/libs/helloworld-1.0.0.jar");

		bundleID = BladeCommandUtil.installBundle(buildOutput);

		BladeCommandUtil.startBundle(bundleID);

		String listBundleOutput = BladeCommandUtil.executeBlade("sh", "lb", "-s", "helloworld");

		System.out.println(listBundleOutput);
	}

	@Test
	public void verifyServiceGradleTemplate () throws Exception {
		File projectPath = BladeCommandUtil.createBladeBundle(testDir, "service", "helloworld");

		File file = new File(projectPath + "/src/main/java/service/FooAction.java");

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

		bundleID = BladeCommandUtil.installBundle(buildOutput);

		BladeCommandUtil.startBundle(bundleID);

		String listBundleOutput = BladeCommandUtil.executeBlade("sh", "lb", "-s", "helloworld");

		System.out.println(listBundleOutput);
	}

	@Test
	public void verifyServiceBuilderGradleTemplate () throws Exception {
		File projectPath = BladeCommandUtil.createBladeBundle(testDir, "servicebuilder", "helloworld");

		BuildTask buildService = GradleRunnerUtil.executeGradleRunner(projectPath, "buildService");
		GradleRunnerUtil.verifyGradleRunnerOutput(buildService);
		BuildTask buildtask = GradleRunnerUtil.executeGradleRunner(projectPath, "build");
		GradleRunnerUtil.verifyGradleRunnerOutput(buildtask);
		GradleRunnerUtil.verifyBuildOutput(projectPath + "/helloworld-api", "helloworld-api-1.0.0.jar");
		GradleRunnerUtil.verifyBuildOutput(projectPath + "/helloworld-service", "helloworld-service-1.0.0.jar");

		File buildOutput = new File(projectPath + "/build/libs/helloworld-1.0.0.jar");

		bundleID = BladeCommandUtil.installBundle(buildOutput);

		BladeCommandUtil.startBundle(bundleID);

		String listBundleOutput = BladeCommandUtil.executeBlade("sh", "lb", "-s", "helloworld");

		System.out.println(listBundleOutput);
	}

	@Test
	public void verifyServiceWrapperGradleTemplate () throws Exception {
		File projectPath = BladeCommandUtil.createBladeBundle(testDir, "servicewrapper", "helloworld");

		BuildTask buildtask = GradleRunnerUtil.executeGradleRunner(projectPath, "build");

		GradleRunnerUtil.verifyGradleRunnerOutput(buildtask);
		GradleRunnerUtil.verifyBuildOutput(projectPath.getPath(), "helloworld-1.0.0.jar");

		File buildOutput = new File(projectPath + "/build/libs/helloworld-1.0.0.jar");

		bundleID = BladeCommandUtil.installBundle(buildOutput);

		BladeCommandUtil.startBundle(bundleID);

		String listBundleOutput = BladeCommandUtil.executeBlade("sh", "lb", "-s", "helloworld");

		System.out.println(listBundleOutput);
	}

	static String bladeJarPath;
	private File testDir;
	private String bundleID;
}