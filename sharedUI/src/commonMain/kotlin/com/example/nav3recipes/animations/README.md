# Animations Recipe

This recipe shows how to override the default animations at the `NavDisplay` level, and at the individual destination level.

## How it works

The `NavDisplay` composable takes `transitionSpec`, `popTransitionSpec`, and `predictivePopTransitionSpec` parameters to define the animations for forward, backward, and predictive back navigation respectively. These animations will be applied to all destinations by default.

In this example, we use `slideInHorizontally` and `slideOutHorizontally` to create a sliding animation for forward and backward navigation.

It is also possible to override these animations for a specific destination by providing a different `transitionSpec` and `popTransitionSpec` to the `entry` composable. In this recipe, `ScreenC` has a custom vertical slide animation.
