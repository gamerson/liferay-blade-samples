# Custom Service

This module's `com.liferay.blade.reluctant.service.reference.api.SomeService`
implementation `CustomServiceImpl` can be bound to service references that have
greedy and reluctant policy options.

The following sample modules refer to the `SomeService` service interface:

-   `blade.greedy.service.reference`
-   `blade.reluctant.service.reference`

`CustomServiceImpl` binds to greedy service references if it's the only
`SomeService` implementation or outranks all other implementations.

Here's `CustomServiceImpl`'s `@Component` annotation:

    @Component(
       immediate = true, property = {"service.ranking:Integer=100"},
       service = SomeService.class
       )

The attribute `immediate = true` informs Liferay's OSGi runtime to activate the
component immediately upon resolution. The property
`"service.ranking:Integer=100"` prioritizes the component above all other
`SomeService` service components that have a ranking less than `100`. Lastly,
the component provides service of type `SomeService.class`.

After deploying the module, if `CustomServiceImpl` provides the highest ranked
`SomeService` implementation, then its bound to all greedy `SomeService`
references it satisfies.

![Here's the result of this module's `CustomServiceImpl` out-ranking all other `SomeService` implementations for component `GreedyStaticServiceReferencePortlet`.](images/providing-a-some-service-impl-that-delegates.png)

Note, rather than inheriting from `SomeServiceImpl`, `CustomServiceImpl` refers
to a `SomeServiceImpl` object (via an` @Reference` annotated field) and
delegates to it.