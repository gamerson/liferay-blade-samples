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

package com.liferay.blade.samples.portlet;

import com.liferay.blade.samples.portlet.action.DeleteFooMVCActionCommand;
import com.liferay.blade.samples.servicebuilder.service.FooLocalServiceUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCPortlet;

import javax.portlet.Portlet;
import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.osgi.service.component.annotations.Component;

import java.io.IOException;

/**
 * @author Liferay
 */
public class JSPPortlet extends MVCPortlet {
    @Override
    public void init() throws PortletException {
        super.init();

        // NOTE: If you have problems finding the portlet name, this can be one way to get it.
        _log.info("Need to render portlet name [" + this.getPortletName() + "].");
    }

    @Override
    public void render(RenderRequest request, RenderResponse response)
            throws IOException, PortletException {

        //set service bean
        request.setAttribute("fooLocalService", FooLocalServiceUtil.getService());

        super.render(request, response);
    }

    private static final Log _log =
            LogFactoryUtil.getLog(JSPPortlet.class);
}