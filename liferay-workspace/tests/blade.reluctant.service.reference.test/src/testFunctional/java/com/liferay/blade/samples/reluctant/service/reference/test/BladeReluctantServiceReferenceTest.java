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

package com.liferay.blade.samples.reluctant.service.reference.test;

import com.liferay.arquillian.portal.annotation.PortalURL;
import com.liferay.portal.kernel.exception.PortalException;

import java.io.File;

import java.net.URL;

import org.jboss.arquillian.container.test.api.Deployment;
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
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * @author Liferay
 */
@RunAsClient
@RunWith(Arquillian.class)
public class BladeReluctantServiceReferenceTest {

	@Deployment
	public static JavaArchive create() throws Exception {
		final File jarFile = new File(System.getProperty("jarFile"));

		return ShrinkWrap.createFromZipFile(JavaArchive.class, jarFile);
	}

	@Test
	public void testBladeSampleGreedyStaticServiceReferencePortlet() {
		_webDriver.get(_greedyPortletURL.toExternalForm());

		Assert.assertTrue(
			"Portlet was not deployed",
			isVisible(_bladeSampleGreedyStaticServiceReferencePortlet));
		Assert.assertTrue(
			"Expected Greedy Static Service Reference Portlet, but saw " +
				_greedyPortletTitle.getText(),
			_greedyPortletTitle.getText().contentEquals(
				"Greedy Static Service Reference Portlet"));
		Assert.assertTrue(
			_greedyPortletBody.getText(),
			_greedyPortletBody.getText().contentEquals(
				"I'm calling service ...\ncom.liferay.blade.custom.service." +
					"CustomServiceImpl, which delegates to com.liferay.blade." +
						"reluctant.service.reference.impl.SomeServiceImpl"));
	}

	@Test
	public void testBladeSampleReluctantStaticServiceReferencePortlet()
		throws PortalException {

		_webDriver.get(_reluctantPortletURL.toExternalForm());

		Assert.assertTrue(
			"Portlet was not deployed",
			isVisible(_bladeSampleReluctantServiceReferencePortlet));
		Assert.assertTrue(
			"Expected Reluctant Static Service Reference Portlet, but saw " +
				_reluctantPortletTitle.getText(),
			_reluctantPortletTitle.getText().contentEquals(
				"Reluctant Static Service Reference Portlet"));
		Assert.assertTrue(
			_reluctantPortletBody.getText(),
			_reluctantPortletBody.getText().contentEquals(
				"I'm calling service ...\ncom.liferay.blade.reluctant." +
					"service.reference.impl.SomeServiceImpl"));
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

	@FindBy(xpath = "//div[contains(@id,'com_liferay_blade_greedy_service_reference_portlet_GreedyStaticServiceReferencePortlet')]")
	private WebElement _bladeSampleGreedyStaticServiceReferencePortlet;

	@FindBy(xpath = "//div[contains(@id,'com_liferay_blade_reluctant_service_reference_portlet_ReluctantServiceReferencePortlet')]")
	private WebElement _bladeSampleReluctantServiceReferencePortlet;

	@FindBy(xpath = "//div[contains(@id,'com_liferay_blade_greedy_service_reference_portlet_GreedyStaticServiceReferencePortlet')]//..//div/div")
	private WebElement _greedyPortletBody;

	@FindBy(xpath = "//div[contains(@id,'com_liferay_blade_greedy_service_reference_portlet_GreedyStaticServiceReferencePortlet')]//..//h2")
	private WebElement _greedyPortletTitle;

	@PortalURL("com_liferay_blade_greedy_service_reference_portlet_GreedyStaticServiceReferencePortlet")
	private URL _greedyPortletURL;

	@FindBy(xpath = "//div[contains(@id,'com_liferay_blade_reluctant_service_reference_portlet_ReluctantServiceReferencePortlet')]//..//div/div")
	private WebElement _reluctantPortletBody;

	@FindBy(xpath = "//div[contains(@id,'com_liferay_blade_reluctant_service_reference_portlet_ReluctantServiceReferencePortlet')]//..//h2")
	private WebElement _reluctantPortletTitle;

	@PortalURL("com_liferay_blade_reluctant_service_reference_portlet_ReluctantServiceReferencePortlet")
	private URL _reluctantPortletURL;

	@Drone
	private WebDriver _webDriver;

}