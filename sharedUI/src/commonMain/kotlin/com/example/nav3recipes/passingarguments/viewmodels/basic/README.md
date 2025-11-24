# Passing Arguments to ViewModels (Basic)

This recipe demonstrates how to pass navigation arguments (keys) to a `ViewModel` using a custom `ViewModelProvider.Factory`.

## How it works

1.  A custom `ViewModelProvider.Factory` is created that takes the navigation key as a constructor parameter.
2.  Inside the `entry` composable, `viewModel(factory = ...)` is used to create the `ViewModel` instance, passing the current navigation key to the factory. This makes the navigation key available to the `ViewModel`.

**Note**: The `rememberViewModelStoreNavEntryDecorator` is added to the `NavDisplay`'s `entryDecorators`. This ensures that `ViewModel`s are correctly scoped to their corresponding `NavEntry`, so that a new `ViewModel` instance is created for each unique navigation key.