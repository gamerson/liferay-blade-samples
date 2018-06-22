<%--
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
--%>

<%@ include file="/init.jsp" %>

<%
String redirect = ParamUtil.getString(request, "redirect");

long barId = ParamUtil.getLong(request, "barId");

Bar bar = null;

if (barId > 0) {
	bar = barLocalService.getBar(barId);
}
%>

<aui:form action="<%= renderResponse.createActionURL() %>" method="post" name="fm">
	<aui:input name="<%= Constants.CMD %>" type="hidden" value="<%= (bar == null) ? Constants.ADD : Constants.UPDATE %>" />
	<aui:input name="redirect" type="hidden" value="<%= currentURL %>" />
	<aui:input name="barId" type="hidden" value="<%= barId %>" />

	<liferay-ui:header
		backURL="<%= redirect %>"
		title='<%= (bar != null) ? bar.getField1() : "new-bar" %>'
	/>

	<liferay-ui:asset-categories-error />

	<liferay-ui:asset-tags-error />

	<aui:model-context bean="<%= bar %>" model="<%= Bar.class %>" />

	<aui:fieldset>
		<aui:input name="field1" />

		<aui:input name="field2" />

		<aui:input name="field3" />

		<aui:input name="field4" />

		<aui:input name="field5" />

		<liferay-ui:custom-attributes-available className="<%= Bar.class.getName() %>">
			<liferay-ui:custom-attribute-list
				className="<%= Bar.class.getName() %>"
				classPK="<%= (bar != null) ? bar.getBarId() : 0 %>"
				editable="<%= true %>"
				label="<%= true %>"
			/>
		</liferay-ui:custom-attributes-available>

		<aui:input name="categories" type="assetCategories" />

		<aui:input name="tags" type="assetTags" />
	</aui:fieldset>

	<aui:button-row>
		<aui:button type="submit" />

		<aui:button href="<%= redirect %>" type="cancel" />
	</aui:button-row>
</aui:form>