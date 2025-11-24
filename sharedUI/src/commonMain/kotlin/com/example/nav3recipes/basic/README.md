# Basic Recipe

This recipe shows a basic example of how to use the Navigation 3 API with two screens.

## How it works

This example defines two routes: `RouteA` and `RouteB`. `RouteA` is a `data object` representing a simple screen, while `RouteB` is a `data class` that takes an `id` as a parameter.

A `mutableStateListOf<Any>` is used to manage the navigation back stack.

The `NavDisplay` composable is used to display the current screen. Its `entryProvider` parameter is a lambda that takes a route from the back stack and returns a `NavEntry`. Inside the `entryProvider`, a `when` statement is used to determine which composable to display based on the route.

To navigate from `RouteA` to `RouteB`, we simply add a `RouteB` instance to the back stack. The `id` is passed as an argument to the `RouteB` data class.
