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

package com.liferay.blade.samples.portlet.action;


import com.liferay.blade.samples.servicebuilder.service.FooLocalService;
import com.liferay.blade.samples.servicebuilder.service.FooLocalServiceUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.portlet.bridges.mvc.BaseMVCActionCommand;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCActionCommand;
import com.liferay.portal.kernel.util.ParamUtil;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;

/**
 * @author Liferay
 */
@Component(
        immediate = true,
        property = {
                "javax.portlet.name=bladeliferaymvc",
                "mvc.command.name=/deleteFoo"
        },
        service = MVCActionCommand.class
)
public class DeleteFooMVCActionCommand extends BaseMVCActionCommand {
    @Override
    protected void doProcessAction(ActionRequest actionRequest, ActionResponse actionResponse) throws Exception {

        _log.info("Deleting a new foo...");

        long fooId = ParamUtil.getLong(actionRequest, "fooId");

        getFooLocalService().deleteFoo(fooId);
    }

    public FooLocalService getFooLocalService() {
        return _fooLocalService;
    }

    @Reference
    private volatile FooLocalService _fooLocalService;

    private static final Log _log =
            LogFactoryUtil.getLog(DeleteFooMVCActionCommand.class);
}
