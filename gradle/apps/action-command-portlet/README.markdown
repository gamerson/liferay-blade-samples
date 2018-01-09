# Action Command Portlet

**Template project description**: Demonstrates the `MVCActionCommand` extension
point. It integrates the action command named `greet` with portlet `greeter`. To
see how this example works, a portlet plugin with a portlet named **greeter**
(`javax.portlet.name='greeter'`) should be deployed. The command adds a key
`greeting_message` to Liferay `SessionMessages`, along with a session attribute
`GREETER_MESSAGE`. You can independently deploy the bundle
`blade.portlet.actioncommand` (i.e., refresh the bundle without the need to
redeploy the Portlet plugin).

For more details on how to use Module Logging, see the following
[reference article](https://dev.liferay.com/develop/tutorials/-/knowledge_base/7-0/adjusting-module-logging)
on Liferay's Developer Network.

HINT: If you're using Tomcat in a Liferay Workspace, you can deploy portal-log4j-ext.xml in [Liferay's sources: portal-impl/classes/META-INF/](https://github.com/liferay/liferay-blade-samples/tree/master/liferay-workspace/configs/common/tomcat-8.0.32/webapps/ROOT/WEB-INF/classes/META-INF/)