/**
 * Copyright 2000-present Liferay, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.liferay.blade.samples.servicebuilder.test;

import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.OperateOnDeployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;

import com.liferay.arquillian.portal.annotation.PortalURL;
import com.liferay.portal.kernel.exception.PortalException;

/**
 * @author Liferay
 */
@RunAsClient
@RunWith(Arquillian.class)
public class BladeServiceBuilderTest {

	@Deployment(name="portlet")
	public static JavaArchive create() throws Exception {
		final File jarFile = new File(System.getProperty("jarFile"));

		return ShrinkWrap.createFromZipFile(JavaArchive.class, jarFile);
	}

	@Test
	@OperateOnDeployment("portlet")
	public void testAddPortletAction()
		throws InterruptedException, IOException, PortalException {

		_browser.get(_portlerURL.toExternalForm());

		_firstParamter.clear();

		_firstParamter.sendKeys("2");

		_secondParameter.clear();

		_secondParameter.sendKeys("3");

		_add.click();

		Thread.sleep(1000);

		Assert.assertEquals("5", _result.getText());
	}

	@Test
	@OperateOnDeployment("portlet")
	public void testInstallPortlet() throws IOException, PortalException {
		_browser.get(_portlerURL.toExternalForm());

		final String bodyText = _browser.getPageSource();

		Assert.assertTrue(
			"The portlet is not well deployed",
			bodyText.contains("Sample Portlet is deployed."));
	}

	@FindBy(css = "button[type=submit]")
	private WebElement _add;

	@Drone
	private WebDriver _browser;

	@FindBy(css = "input[id$='firstParameter']")
	private WebElement _firstParamter;

	@PortalURL("sample_portlet")
	private URL _portlerURL;

	@FindBy(css = "span[class='result']")
	private WebElement _result;

	@FindBy(css = "input[id$='secondParameter']")
	private WebElement _secondParameter;
}
