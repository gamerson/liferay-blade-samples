/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.liferay.blade.samples.servicebuilder.test;

import java.io.File;
import java.net.URL;
import java.util.List;

import org.jboss.arquillian.showcase.extension.lifecycle.api.AfterDeploy;
import org.jboss.arquillian.showcase.extension.lifecycle.api.BeforeDeploy;
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
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.osgi.framework.dto.BundleDTO;

import com.liferay.arquillian.portal.annotation.PortalURL;
import com.liferay.portal.kernel.exception.PortalException;

/**
 * @author Liferay
 */
@RunWith(Arquillian.class)
public class BladeServiceBuilderTest {
	
	@BeforeDeploy
	public static void deployDependencies() throws Exception {
		final File dependency1 = new File(System.getProperty("dependency1"));
		final File dependency2 = new File(System.getProperty("dependency2"));
		final File dependency3 = new File(System.getProperty("dependency3"));
		
		System.out.println(dependency1.toString());
		System.out.println(dependency2.toString());
		System.out.println(dependency3.toString());
		
		long bundle1 = new JMXBundleDeployer().deploy(dependency1BSN, dependency1);
		long bundle2 = new JMXBundleDeployer().deploy(dependency2BSN, dependency2);
		long bundle3 = new JMXBundleDeployer().deploy(dependency3BSN, dependency3);
		
		System.out.println(bundle1);
		System.out.println(bundle2);
		System.out.println(bundle3);

	}
	
	@AfterDeploy
	public static void waitForDeployedDependencies() throws InterruptedException {	
		boolean bundleActivated = false;
		
		while (!bundleActivated) {
			try {
				BundleDTO[] bundleList = new JMXBundleDeployer().listBundles();
				
				for (BundleDTO string : bundleList) {
					if (string.symbolicName.matches(dependency1BSN)) {						
						bundleActivated = true;
						break;
					}
				}
			}
			catch (Exception e){	
			}
			Thread.sleep(100);
		}
	}
	
	@AfterClass
	public static void tearDown() throws Exception {
		new JMXBundleDeployer().uninstall(dependency1BSN);
		new JMXBundleDeployer().uninstall(dependency2BSN);
		new JMXBundleDeployer().uninstall(dependency3BSN);
	}

	@Deployment(testable = true)
	public static JavaArchive create() throws Exception {
		final File jarFile = new File(System.getProperty("jarFile"));

		return ShrinkWrap.createFromZipFile(JavaArchive.class, jarFile);
	}
	
	public void customClick(WebDriver webDriver, WebElement webElement) {
		Actions action = new Actions(webDriver);

		action.moveToElement(webElement).build().perform();

		WebDriverWait wait = new WebDriverWait(webDriver, 5);

		WebElement element = wait.until(
			ExpectedConditions.visibilityOf(webElement));

		element.click();
	}
	
	@RunAsClient
	@Test
	public void testCreateFoo() throws InterruptedException, PortalException {
		_webDriver.get(_portletURL.toExternalForm());

		customClick(_webDriver, _addButton);

		Assert.assertTrue(isVisible(_field1Form));

		_field1Form.sendKeys("Hello");

		_field5Form.clear();

		_field5Form.sendKeys("World");

		customClick(_webDriver, _saveButton);

		Assert.assertTrue(isVisible(_table));

		Assert.assertTrue(_table.getText().contains("Hello"));

		Assert.assertTrue(_table.getText().contains("World"));
	}

	@RunAsClient
	@Test
	public void testDeleteFoo() throws InterruptedException, PortalException {
		_webDriver.get(_portletURL.toExternalForm());

		List<WebElement> rows = _webDriver.findElements(
			By.xpath(
				"//table[contains(@data-searchcontainerid,'foosSearchContainer')]/tbody/tr"));

		int originalRows = rows.size();

		Assert.assertTrue(isVisible(_lfrIconMenu));

		customClick(_webDriver, _lfrIconMenu);

		JavascriptExecutor javascriptExecutor = (JavascriptExecutor)_webDriver;

		Assert.assertTrue(isVisible(_lfrMenuDelete));

		String source = _webDriver.getPageSource();

		String executescript = source.substring(
			source.indexOf("item-remove") + 1,
			source.indexOf("foosSearchContainer__10__menu__delete"));

		String script = executescript.substring(
			executescript.indexOf("submitForm") - 1,
			executescript.indexOf("else") - 2);

		javascriptExecutor.executeScript(script);

		Thread.sleep(1000);

		_webDriver.navigate().refresh();

		Assert.assertTrue(isVisible(_table));

		rows = _webDriver.findElements(
			By.xpath(
				"//table[contains(@data-searchcontainerid,'foosSearchContainer')]/tbody/tr"));

		int newRows = rows.size();

		int expectedFoos = originalRows - 1;

		Assert.assertTrue(
			"Expected " + expectedFoos + " foos, but saw " + newRows + " foos",
			newRows == expectedFoos);
	}

	@RunAsClient
	@Test
	public void testReadFoo() throws PortalException {
		_webDriver.get(_portletURL.toExternalForm());

		Assert.assertTrue(isVisible(_firstRowField1));

		Assert.assertTrue(_firstRowField1.getText().contains("new field1 entry"));

		Assert.assertTrue(_secondRowField1.getText().contains("new field1 entry"));
	}

	@RunAsClient
	@Test
	public void testUpdateFoo() throws InterruptedException, PortalException {
		_webDriver.get(_portletURL.toExternalForm());

		Assert.assertTrue(isVisible(_lfrIconMenu));

		customClick(_webDriver, _lfrIconMenu);

		Assert.assertTrue(isVisible(_lfrMenuEdit));

		customClick(_webDriver, _lfrMenuEdit);

		Assert.assertTrue(isVisible(_field1Form));

		_field1Form.clear();

		_field1Form.sendKeys("field1 with Updated Name");

		customClick(_webDriver, _saveButton);

		Assert.assertTrue(isVisible(_table));

		Assert.assertTrue(_table.getText().contains("field1 with Updated Name"));
	}

	protected static boolean isAlertPresent(WebDriver webDriver) {
		WebDriverWait webDriverWait = new WebDriverWait(webDriver, 3);

		try {
			ExpectedCondition<Alert> alert =
				ExpectedConditions.alertIsPresent();

			webDriverWait.until(alert);

			return true;
		}
		catch (org.openqa.selenium.TimeoutException te) {
			return false;
		}
	}

	protected boolean isVisible(WebElement webelement) {
		WebDriverWait webDriverWait = new WebDriverWait(_webDriver, 5);

		try {
			webDriverWait.until(ExpectedConditions.visibilityOf(webelement));

			return true;
		}
		catch (org.openqa.selenium.TimeoutException te) {
			return false;
		}
	}

	@FindBy(xpath = "//span[@class='lfr-btn-label']")
	private WebElement _addButton;

	@FindBy(css = "input[id$='field1']")
	private WebElement _field1Form;

	@FindBy(css = "input[id$='field5']")
	private WebElement _field5Form;

	@FindBy(xpath = "//div[contains(@id,'_com_liferay_blade_samples_servicebuilder_web')]/table/tbody/tr/td[2]")
	private WebElement _firstRowField1;

	@FindBy(xpath = "//a[contains(@id,'foosSearchContainer')]")
	private WebElement _lfrIconMenu;

	@FindBy(xpath = "//ul[contains(@class,'dropdown-menu')]/li[2]/a[contains(.,'Delete')]")
	private WebElement _lfrMenuDelete;

	@FindBy(xpath = "//ul[contains(@class,'dropdown-menu')]/li[1]/a[contains(.,'Edit')]")
	private WebElement _lfrMenuEdit;

	@PortalURL("com_liferay_blade_samples_servicebuilder_web")
	private URL _portletURL;

	@FindBy(css = "button[type=submit]")
	private WebElement _saveButton;

	@FindBy(xpath = "//div[contains(@id,'_com_liferay_blade_samples_servicebuilder_web')]/table/tbody/tr[2]/td[2]")
	private WebElement _secondRowField1;

	@FindBy(xpath = "//table[contains(@data-searchcontainerid,'foosSearchContainer')]")
	private WebElement _table;

	@Drone
	private WebDriver _webDriver;
	
	private static String dependency1BSN = "blade.servicebuilder.api";
	private static String dependency2BSN = "blade.servicebuilder.svc";
	private static String dependency3BSN = "blade.servicebuilder.web";

}