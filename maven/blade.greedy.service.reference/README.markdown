# Greedy Static Service Reference

This module's `GenericPortlet` `GreedyStaticServiceReferencePortlet` consumes
an OSGi service `com.liferay.blade.reluctant.service.reference.api.SomeService` and prints a message in the portlet indicating the
bound `SomeService` implementation class it calls.

![The portlet calls on `SomeService` implementation `SomeServiceImpl`, by default.](images/using-default-service-impl.png)

This module depends on module `blade.reluctant.service.reference` for its
`SomeService` interface.

`GreedyStaticServiceReferencePortlet`'s field `_someService` uses an
`@Reference` annotation, whose policy is static and greedy.

	@Reference (policyOption = ReferencePolicyOption.GREEDY, unbind = "-")
	private SomeService _someService;

The annotation's attribute setting `unbind = "-"` indicates that the registrator
class has no method for unbinding the service. The portlet is bound to
`SomeService` implementation `SomeServiceImpl` by default.

This module is intended to be used with module `blade.custom.service`, which
provides a `SomeService` implementation `CustomServiceImpl`. On deploying
`blade.custom.service`'s higher ranked `SomeService` implementation, this
portlet uses it.

![Deploying the Configuration Admin file binds the portlet's `SomeService` reference to `CustomServiceImpl`.](images/using-custom-higher-ranking-service-impl.png)

`GreedyStaticServiceReferencePortlet` refers to and calls on
`CustomServiceImpl`. Additionally, the `CustomServiceImpl` refers to a
`SomeServiceImpl` object (via an` @Reference` annotated field) and delegates to
it.