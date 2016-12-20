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

package blade.document.action.displaycontext;

import com.liferay.document.library.display.context.BaseDLViewFileVersionDisplayContext;
import com.liferay.document.library.display.context.DLViewFileVersionDisplayContext;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.repository.model.FileVersion;
import com.liferay.portal.kernel.servlet.HttpMethods;
import com.liferay.portal.kernel.servlet.taglib.ui.Menu;
import com.liferay.portal.kernel.servlet.taglib.ui.ToolbarItem;
import com.liferay.portal.kernel.servlet.taglib.ui.URLMenuItem;
import com.liferay.portal.kernel.servlet.taglib.ui.URLToolbarItem;
import com.liferay.portal.kernel.settings.PortletInstanceSettingsLocator;
import com.liferay.portal.kernel.settings.Settings;
import com.liferay.portal.kernel.settings.SettingsException;
import com.liferay.portal.kernel.settings.SettingsFactoryUtil;
import com.liferay.portal.kernel.settings.TypedSettings;
import com.liferay.portal.kernel.theme.PortletDisplay;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.PortletKeys;
import com.liferay.portal.kernel.util.WebKeys;

import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author liferay
 */
public class BladeActionDisplayContext
	extends BaseDLViewFileVersionDisplayContext {

	public BladeActionDisplayContext(
		UUID uuid, DLViewFileVersionDisplayContext parentDLDisplayContext,
		HttpServletRequest request, HttpServletResponse response,
		FileVersion fileVersion) {

		super(uuid, parentDLDisplayContext, request, response, fileVersion);

		_themeDisplay = (ThemeDisplay)request.getAttribute(
			WebKeys.THEME_DISPLAY);
	}

	public Menu getMenu() throws PortalException {
		Menu menu = super.getMenu();

		if (_showAction()) {
			URLMenuItem urlMenuItem = new URLMenuItem();

			urlMenuItem.setMethod(HttpMethods.GET);
			urlMenuItem.setKey("blade-action");
			urlMenuItem.setLabel("Blade Action");
			urlMenuItem.setURL("javascript:;");

			menu.getMenuItems().add(urlMenuItem);
		}

		return menu;
	}

	@Override
	public List<ToolbarItem> getToolbarItems() throws PortalException {
		List<ToolbarItem> toolbarItems = super.getToolbarItems();

		URLToolbarItem toolbarItem = new URLToolbarItem();

		toolbarItem.setKey("blade-action");
		toolbarItem.setLabel("Blade Action");
		toolbarItem.setURL("javascript:;");

		toolbarItems.add(toolbarItem);

		return toolbarItems;
	}

	private boolean _showAction() throws SettingsException {
		PortletDisplay portletDisplay = _themeDisplay.getPortletDisplay();

		String portletName = portletDisplay.getPortletName();

		if (portletName.equals(PortletKeys.DOCUMENT_LIBRARY_ADMIN)) {
			return true;
		}

		Settings settings = SettingsFactoryUtil.getSettings(
			new PortletInstanceSettingsLocator(
				_themeDisplay.getLayout(), portletDisplay.getId()));

		TypedSettings typedSettings = new TypedSettings(settings);

		return typedSettings.getBooleanValue("showActions");
	}

	private ThemeDisplay _themeDisplay;

}