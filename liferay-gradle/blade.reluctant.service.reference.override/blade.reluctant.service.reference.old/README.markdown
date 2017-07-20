# Override My Service Reference Portlet

This module's `GenericPortlet` `OverrideMyServiceReferencePortlet` consumes an
OSGi service `SomeService` and prints a message in the portlet indicating the
bound `SomeService` implementation class.

![The portlet calls on `SomeService` implementation `SomeServiceImpl`, by default.](images/overriding-service-refs-default-impl.png)

`OverrideMyServiceReferencePortlet`'s field `_someService` uses an `@Reference`
annotation, whose policy is static and reluctant. The portlet is bound to
`SomeService` implementation `SomeServiceImpl` by default.

This module is intended to be used with module `blade.reluctant.service.reference.new`,
which has `SomeService` implementation `CustomServiceImpl`. That module's OSGi
configuration admin file
`configurations/com.liferay.blade.reluctant.service.reference.old.portlet.OverrideMyServiceReference.*`
can be deployed to the Liferay instance's `osgi/configs/` folder to configure
`OverrideMyServiceReferencePortlet`'s `_someService` field for binding to
`CustomServiceImpl`.

![Result of overriding the service reference.](images/overriding-service-refs-result.png)

As a result of the deployed configuration, `OverrideMyServiceReferencePortlet`
refers to and invokes the `CustomServiceImpl`. Additionally, the
`CustomServiceImpl` refers to a `SomeServiceImpl` object (via an` @Reference`
annotated field) and delegates to it.