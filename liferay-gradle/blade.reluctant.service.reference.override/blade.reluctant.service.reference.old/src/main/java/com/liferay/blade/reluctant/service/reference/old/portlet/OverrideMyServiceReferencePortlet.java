package com.liferay.blade.reluctant.service.reference.old.portlet;

import com.liferay.blade.reluctant.service.reference.old.service.api.SomeService;

import java.io.IOException;
import java.io.PrintWriter;

import javax.portlet.GenericPortlet;
import javax.portlet.Portlet;
import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author James Hinkey
 */
@Component(
	immediate = true,
	property = {
		"com.liferay.portlet.display-category=category.sample",
		"com.liferay.portlet.instanceable=true",
		"javax.portlet.display-name=Override My Service Reference",
		"javax.portlet.security-role-ref=power-user,user"
	},
	service = Portlet.class
)
public class OverrideMyServiceReferencePortlet extends GenericPortlet {

	@Override
	protected void doView(RenderRequest request, RenderResponse response)
		throws IOException, PortletException {

		PrintWriter printWriter = response.getWriter();

		printWriter.println("I'm calling a service ...<br>");

		printWriter.println(_someService.doSomething());
	}

	@Reference (unbind = "-")
	private SomeService _someService;

}