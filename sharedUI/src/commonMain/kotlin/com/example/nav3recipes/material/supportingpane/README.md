# Material Supporting Pane Recipe

This recipe demonstrates how to create an adaptive layout with a main pane and a supporting pane using the `SupportingPaneSceneStrategy` from the Material 3 Adaptive library. This layout is useful for displaying supplementary content alongside the main content on larger screens.

## How it works

This example has three destinations: `MainVideo`, `RelatedVideos`, and `Profile`.

### `SupportingPaneSceneStrategy`

The `rememberSupportingPaneSceneStrategy` provides the logic for this adaptive layout.

-   **Pane Roles**: Each destination is assigned a role using metadata:
    -   `SupportingPaneSceneStrategy.mainPane()`: For the primary content. This pane is always visible.
    -   `SupportingPaneSceneStrategy.supportingPane()`: For the supplementary content. This pane is shown alongside the main pane on larger screens.
    -   `SupportingPaneSceneStrategy.extraPane()`: For tertiary content that can be displayed alongside the supporting pane on even larger screens.

-   **Adaptive Layout**: The `SupportingPaneSceneStrategy` automatically handles the layout. On smaller screens, only the main pane is shown. On larger screens, the supporting pane is shown next to the main pane.

-   **Back Navigation**: The `BackNavigationBehavior` is customized in this example to `PopUntilCurrentDestinationChange`. This means that when the user presses the back button, the supporting pane will be dismissed, revealing the main pane underneath.

-   **Navigation**: Navigation is handled by adding and removing destinations from the back stack. The `SupportingPaneSceneStrategy` observes these changes and adjusts the layout accordingly.
