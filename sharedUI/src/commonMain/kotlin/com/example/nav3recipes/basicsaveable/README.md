# Basic Saveable Recipe

This recipe shows a basic example of how to create a persistent back stack that survives configuration changes.

## How it works

To make the back stack persistent, we use the `rememberNavBackStack` function. This function creates and remembers the back stack across configuration changes (e.g., screen rotation).

A requirement for using `rememberNavBackStack` is that the navigation keys (routes) must be serializable. In this example, `RouteA` and `RouteB` are annotated with `@Serializable` and implement the `NavKey` interface.

This example uses a `when` statement within the `entryProvider` to map routes to their corresponding composables, but it could also be used with the `entryProvider` DSL.
