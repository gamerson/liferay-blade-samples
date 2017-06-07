# Reluctant Static Service Reference

This module's `GenericPortlet` `ReluctantStaticServiceReferencePortlet` consumes
an OSGi service `com.liferay.blade.reluctant.service.reference.api.SomeService` and prints a message in the portlet indicating the
bound `SomeService` implementation class it calls.

![The portlet calls on `SomeService` implementation `SomeServiceImpl`, by default.](images/using-default-service-impl.png)

`ReluctantStaticServiceReferencePortlet`'s field `_someService` uses an
`@Reference` annotation, whose policy is static and reluctant by default.

    @Reference (unbind = "-")
    private SomeService _someService;

The annotation's attribute setting `unbind = "-"` indicates that the registrator
class has no method for unbinding the service. The portlet is bound to
`SomeService` implementation `SomeServiceImpl` by default.

This module is intended to be used with module `blade.custom.service`, which provides
`SomeService` implementation `CustomServiceImpl`. That module's Configuration
Admin file
`configurations/com.liferay.blade.reluctant.service.reference.portlet.ReluctantServiceReferencePortlet.config`
can be deployed to the Liferay instance's `osgi/configs/` folder to configure
`ReluctantStaticServiceReferencePortlet`'s `_someService` field to bind to
`CustomServiceImpl`.

As a result of deploying the configuration,
`ReluctantStaticServiceReferencePortlet` refers to and calls on
`CustomServiceImpl`.

![Deploying the Configuration Admin file binds the portlet's `SomeService` reference to `CustomServiceImpl`.](images/using-custom-service-impl.png)

Additionally, the `CustomServiceImpl` refers to a `SomeServiceImpl` object (via
an` @Reference` annotated field) and delegates to it.