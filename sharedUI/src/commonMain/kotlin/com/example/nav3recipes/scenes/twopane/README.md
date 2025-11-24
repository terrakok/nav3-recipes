# Two-Pane Scene Recipe

This example shows how to create a two pane layout using the Scenes API.

A `TwoPaneSceneStrategy` will return a `TwoPaneScene` if:

-   the window width is over 600dp
-   the last two nav entries on the back stack have indicated that they support being displayed in a `TwoPaneScene` in their metadata.

See `TwoPaneScene.kt` for more implementation details.
