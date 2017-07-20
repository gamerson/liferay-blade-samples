# Overriding Service Reference Module

This module's `SomeService` implementation `CustomServiceImpl` can be bound to
`com.liferay.blade.reluctant.service.reference.old.portlet.OverrideMyServiceReference`'s field
`_someService`. OSGi configuration admin file
`configurations/com.liferay.blade.reluctant.service.reference.old.portlet.OverrideMyServiceReference.*`
can be deployed to the Liferay instance's `osgi/configs/` folder to configure
`OverrideMyServiceReferencePortlet`'s `_someService` field to bind to
`CustomServiceImpl`.

**Note:** If you're running Liferay DXP DE 7.0 Fix Pack 8 or later or Liferay
Portal CE 7.0 GA4 or later, use the `.config` file; otherwise, use the `.cfg`
file.

On binding to that field, the portlet's message indicates it's using
`CustomServiceImpl` as its `SomeService` implementation.

![Result of overriding the service reference.](images/overriding-service-refs-result.png)

Note, rather than inheriting from `SomeServiceImpl`, `CustomServiceImpl` refers
to a `SomeServiceImpl` object (via an` @Reference` annotated field) and
delegates to it.