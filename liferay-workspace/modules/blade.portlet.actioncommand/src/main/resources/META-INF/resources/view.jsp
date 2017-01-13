<%--
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
--%>

<%@ include file="/init.jsp" %>

<%
	ActionRequest actionRequest =
	(ActionRequest)actionRequest.getAttribute(“GREETER_MESSAGE”);
%>

<liferay-ui:message key="blade_portlet_actioncommand_GreeterPortlet.caption" />

<liferay-portlet:actionURL name="greet" var="greetURL" />

<aui:form action="<%= greetURL %>" method="post" name="fm">
	<aui:input name="name" type="text" />

	<aui:button-row>
		<aui:button type="submit"></aui:button>
	</aui:button-row>
</aui:form>