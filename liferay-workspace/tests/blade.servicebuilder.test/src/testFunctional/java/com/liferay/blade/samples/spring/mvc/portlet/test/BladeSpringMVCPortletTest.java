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

package com.liferay.blade.samples.spring.mvc.portlet.test;

import aQute.remote.util.JMXBundleDeployer;

import com.liferay.arquillian.portal.annotation.PortalURL;
import com.liferay.blade.sample.test.functional.utils.BladeSampleFunctionalActionUtil;
import com.liferay.blade.samples.integration.test.utils.BladeCLIUtil;
import com.liferay.portal.kernel.exception.PortalException;

import java.io.File;

import java.net.URL;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * @author Liferay
 */
@RunAsClient
@RunWith(Arquillian.class)
public class BladeSpringMVCPortletTest {

	@AfterClass
	public static void cleanUp() throws Exception {
		new JMXBundleDeployer().uninstall(_fooApiJarBSN);
		new JMXBundleDeployer().uninstall(_fooServiceJarBSN);
		BladeCLIUtil.uninstallBundle(_springmvcbundleId);
	}

	@Deployment
	public static JavaArchive create() throws Exception {
		final File fooApiJar = new File(System.getProperty("fooApiJarFile"));
		final File fooServiceJar = new File(
			System.getProperty("fooServiceJarFile"));
		final File fooWebJar = new File(System.getProperty("fooWebJarFile"));
		final File springmvcPortletWar = new File(
			System.getProperty("springmvcPortletWarFile"));

		new JMXBundleDeployer().deploy(_fooApiJarBSN, fooApiJar);
		new JMXBundleDeployer().deploy(_fooServiceJarBSN, fooServiceJar);

		_springmvcbundleId = BladeCLIUtil.installBundle(springmvcPortletWar);

		BladeCLIUtil.startBundle(_springmvcbundleId);

		return ShrinkWrap.createFromZipFile(JavaArchive.class, fooWebJar);
	}

	@Test
	public void testSpringBasic() throws InterruptedException, PortalException {
		_webDriver.get(_portletURL.toExternalForm());

		BladeSampleFunctionalActionUtil.implicitWait(_webDriver);

		String windowHandler = _webDriver.getWindowHandle();

		String url = _webDriver.getCurrentUrl();

		BladeSampleFunctionalActionUtil.mouseOverClick(_webDriver, _addButton);

		Assert.assertTrue(
			BladeSampleFunctionalActionUtil.isVisible(_webDriver, _field1Form));

		_field1Form.sendKeys("aSpringDeletableEntry");

		_field5Form.clear();

		_field5Form.sendKeys("aSpringDeletableEntryfield5");

		BladeSampleFunctionalActionUtil.mouseOverClick(_webDriver, _saveButton);

		Thread.sleep(5000);

		_webDriver.navigate().to(url);

		_webDriver.navigate().refresh();

		Assert.assertTrue(
			"Spring Service Builder Table does not contain aSpringDeletableEntry" +
				_table.getText(),
			_table.getText().contains("aSpringDeletableEntry"));

		Assert.assertTrue(
			"Liferay Icon menu is not visible",
			BladeSampleFunctionalActionUtil.isVisible(_webDriver, _lfrIconMenu));

		BladeSampleFunctionalActionUtil.mouseOverClick(_webDriver, _lfrIconMenu);

		Assert.assertTrue(
			"Liferay Menu Edit is not visible",
			BladeSampleFunctionalActionUtil.isVisible(_webDriver, _lfrMenuEdit));

		BladeSampleFunctionalActionUtil.mouseOverClick(_webDriver, _lfrMenuEdit);

		Assert.assertTrue(
			BladeSampleFunctionalActionUtil.isVisible(_webDriver, _field1Form));

		_field1Form.clear();

		_field1Form.sendKeys("Spring Updated Name");

		BladeSampleFunctionalActionUtil.mouseOverClick(_webDriver, _saveButton);

		Thread.sleep(1000);

		_webDriver.navigate().to(url);

		_webDriver.navigate().refresh();

		Assert.assertTrue(
			"Service Builder Table does not contain Spring Updated Name" +
			_table.getText(), _table.getText().contains("Spring Updated Name"));

		Assert.assertTrue(
			"Liferay Icon menu is not visible",
			BladeSampleFunctionalActionUtil.isVisible(_webDriver, _lfrIconMenu));

		BladeSampleFunctionalActionUtil.mouseOverClick(_webDriver, _lfrIconMenu);

		Assert.assertTrue(
			"Liferay Menu Delete is not visible",
			BladeSampleFunctionalActionUtil.isVisible(_webDriver, _lfrMenuDelete));

		BladeSampleFunctionalActionUtil.mouseOverClick(_webDriver, _lfrMenuDelete);

		Assert.assertTrue(
			"Alert is not present!",
			BladeSampleFunctionalActionUtil.isAlertPresent(_webDriver));

		_webDriver.switchTo().window(windowHandler);

		Thread.sleep(1000);

		_webDriver.navigate().to(url);

		_webDriver.navigate().refresh();

		Assert.assertTrue(
			_table.getText(),
			!_table.getText().contains("aSpringDeletableEntry"));
	}

	private static String _fooApiJarBSN = "blade.servicebuilder.api";
	private static String _fooServiceJarBSN = "blade.servicebuilder.svc";
	private static String _springmvcbundleId;

	@FindBy(xpath = "//span[@class='lfr-btn-label']")
	private WebElement _addButton;

	@FindBy(xpath = "//input[contains(@id,'field1')]")
	private WebElement _field1Form;

	@FindBy(xpath = "//input[contains(@id,'field5')]")
	private WebElement _field5Form;

	@FindBy(xpath = "//div[contains(@id,'bladespringmvc_WAR_bladespringmvc')]/table//..//tr/td[6]")
	private WebElement _firstRowField5;

	@FindBy(xpath = "//div[@class='btn-group lfr-icon-menu']/a")
	private WebElement _lfrIconMenu;

	@FindBy(xpath = "//ul[contains(@class,'dropdown-menu')]/li[2]/a[contains(.,'Delete')]")
	private WebElement _lfrMenuDelete;

	@FindBy(xpath = "//ul[contains(@class,'dropdown-menu')]/li[1]/a[contains(.,'Edit')]")
	private WebElement _lfrMenuEdit;

	@PortalURL("bladespringmvc_WAR_bladespringmvc")
	private URL _portletURL;

	@FindBy(xpath = "//button[@type='submit']")
	private WebElement _saveButton;

	@FindBy(xpath = "//div[contains(@id,'bladespringmvc_WAR_bladespringmvc')]/table//..//tr[2]/td[6]")
	private WebElement _secondRowField5;

	@FindBy(xpath = "//table[contains(@data-searchcontainerid,'foosSearchContainer')]")
	private WebElement _table;

	@Drone
	private WebDriver _webDriver;

}