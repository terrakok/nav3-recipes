# Returning a Result (Event-Based)

This recipe demonstrates how to return a result from one screen to a previous screen using an event-based approach.

## How it works

This example uses a `ResultEventBus` to facilitate communication between the screens.

1.  **`ResultEventBus`**: A simple event bus is created and made available to the composables.
2.  **Sending the result**: The screen that produces the result calls `resultBus.sendResult(person)` to send the data back as a one-time event.
3.  **Receiving the result**: The screen that needs the result uses a `ResultEffect` composable to listen for results of a specific type. When a result is received, the effect's lambda is triggered.

This approach is useful for results that are transient and should be handled as one-time events.
