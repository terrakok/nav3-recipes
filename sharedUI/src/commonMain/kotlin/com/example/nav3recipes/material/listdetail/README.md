# Material List-Detail Recipe

This recipe demonstrates how to create an adaptive list-detail layout using the `ListDetailSceneStrategy` from the Material 3 Adaptive library. This layout automatically adjusts to show one, two, or three panes depending on the available screen width.

## How it works

This example has three destinations: `ConversationList`, `ConversationDetail`, and `Profile`.

### `ListDetailSceneStrategy`

The key to this recipe is the `rememberListDetailSceneStrategy`, which provides the logic for the adaptive layout.

-   **Pane Roles**: Each destination is assigned a role using metadata:
    -   `ListDetailSceneStrategy.listPane()`: For the primary (list) content. This pane is always visible. A placeholder can be provided to be shown in the detail pane area when no detail content is selected.
    -   `ListDetailSceneStrategy.detailPane()`: For the secondary (detail) content.
    -   `ListDetailSceneStrategy.extraPane()`: For tertiary content.

-   **Adaptive Layout**: The `ListDetailSceneStrategy` automatically handles the layout. On smaller screens, only one pane is shown at a time. On wider screens, it will show the list and detail panes side-by-side. On very wide screens, it can show all three panes: list, detail, and extra.

-   **Navigation**: Navigation between the panes is handled by adding and removing destinations from the back stack as usual. The `ListDetailSceneStrategy` observes the back stack and adjusts the layout accordingly.
