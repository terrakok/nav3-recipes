# Returning a Result (State-Based)

This recipe demonstrates how to return a result from one screen to a previous screen using a state-based approach.

## How it works

This example uses a `ResultStore` to manage the result as state.

1.  **`ResultStore`**: A `ResultStore` is created and made available to the composables. This store holds the results.
2.  **Setting the result**: The screen that produces the result calls `resultStore.setResult(person)` to save the data in the store.
3.  **Observing the result**: The screen that needs the result calls `resultStore.getResultState<Person?>()` to get a `State` object representing the result. The UI then observes this state and recomposes whenever the result changes.

This approach is suitable when the result should be treated as persistent state that survives recomposition and configuration changes.
