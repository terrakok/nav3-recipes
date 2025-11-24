# Dialog Recipe

This recipe demonstrates how to display a destination as a dialog.

## How it works

To show a destination as a dialog, you need to do two things:

1.  **Use `DialogSceneStrategy`**: Create an instance of `DialogSceneStrategy` and pass it to the `sceneStrategy` parameter of the `NavDisplay` composable.

2.  **Add metadata to the destination**: For the destination that you want to display as a dialog, add `DialogSceneStrategy.dialog()` to its metadata. This is done in the `entry` function. You can also pass a `DialogProperties` object to customize the dialog's behavior and appearance.

In this example, `RouteB` is configured to be a dialog. When you navigate from `RouteA` to `RouteB`, `RouteB` will be displayed in a dialog window.

The content of the dialog can be styled as needed. In this recipe, the content is clipped to have rounded corners.

For more information, see the official documentation on [custom layouts](https://developer.android.com/guide/navigation/navigation-3/custom-layouts).
