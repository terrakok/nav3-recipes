# Bottom Sheet Recipe

This recipe demonstrates how to display a destination as a modal bottom sheet.

## How it works

To show a destination as a bottom sheet, you need to do two things:

1.  **Use `BottomSheetSceneStrategy`**: Create an instance of `BottomSheetSceneStrategy` and pass it to the `sceneStrategy` parameter of the `NavDisplay` composable.

2.  **Add metadata to the destination**: For the destination that you want to display as a bottom sheet, add `BottomSheetSceneStrategy.bottomSheet()` to its metadata. This is done in the `entry` function.

In this example, `RouteB` is configured to be a bottom sheet. When you navigate from `RouteA` to `RouteB`, `RouteB` will be displayed in a modal bottom sheet that slides up from the bottom of the screen.

The content of the bottom sheet can be styled as needed. In this recipe, the content is clipped to have rounded corners.

For more information, see the official documentation on [custom layouts](https://developer.android.com/guide/navigation/navigation-3/custom-layouts).
