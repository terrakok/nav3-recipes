# Conditional Navigation Recipe

This recipe demonstrates how to implement conditional navigation, where certain destinations are only accessible if a condition is met (in this case, if the user is logged in).

## How it works

This example has a `Profile` destination that requires the user to be logged in. If the user is not logged in and attempts to navigate to `Profile`, they are redirected to a `Login` screen. After a successful login, they are automatically navigated to the `Profile` screen.

### `AppBackStack`

The core of this recipe is the custom `AppBackStack` class, which encapsulates the logic for conditional navigation.

-   **`RequiresLogin` interface**: A marker interface, `RequiresLogin`, is used to identify destinations that require the user to be logged in. The `Profile` destination implements this interface.

-   **Redirecting to Login**: When the `add` function is called with a destination that implements `RequiresLogin` and the user is not logged in, `AppBackStack` stores the intended destination and adds the `Login` route to the back stack instead.

-   **Handling Login**: When the `login` function is called, it sets the user's status to logged in. If there is a stored destination that the user was trying to access, it adds that destination to the back stack and removes the `Login` screen.

-   **Handling Logout**: When the `logout` function is called, it sets the user's status to logged out and removes any destinations from the back stack that require the user to be logged in.

This approach provides a clean way to handle conditional navigation by centralizing the logic in a custom back stack implementation.
