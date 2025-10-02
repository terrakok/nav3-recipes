package org.company.app.passingarguments.basicviewmodels

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberSavedStateNavEntryDecorator
import androidx.navigation3.scene.rememberSceneSetupNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import org.company.app.ContentBlue
import org.company.app.ContentGreen
import kotlin.reflect.KClass

/**
 * Passing navigation arguments to a ViewModel.
 *
 * - ViewModelStoreNavEntryDecorator ensures that ViewModels are scoped to the NavEntry
 */
data object RouteA

data class RouteB(val id: String)

@Composable
fun BasicViewModelsCase() {
    val backStack = remember { mutableStateListOf<Any>(RouteA) }

    NavDisplay(
        backStack = backStack,
        onBack = { backStack.removeLastOrNull() },
        // In order to add the `ViewModelStoreNavEntryDecorator` (see comment below for why)
        // we also need to add the default `NavEntryDecorator`s as well. These provide
        // extra information to the entry's content to enable it to display correctly
        // and save its state.
        entryDecorators = listOf(
            rememberSceneSetupNavEntryDecorator(),
            rememberSavedStateNavEntryDecorator(),
//            rememberViewModelStoreNavEntryDecorator() TODO
        ),
        entryProvider = entryProvider {
            entry<RouteA> {
                ContentGreen("Welcome to Nav3") {
                    LazyColumn {
                        items(10) { i ->
                            Button(onClick = {
                                backStack.add(RouteB("$i"))
                            }) {
                                Text("$i")
                            }
                        }
                    }
                }
            }
            entry<RouteB> { key ->
                // Note: We need a new ViewModel for every new RouteB instance. Usually
                // we would need to supply a `key` String that is unique to the
                // instance, however, the ViewModelStoreNavEntryDecorator (supplied
                // above) does this for us, using `NavEntry.contentKey` to uniquely
                // identify the viewModel.
                //
                // tl;dr: Make sure you use rememberViewModelStoreNavEntryDecorator()
                // if you want a new ViewModel for each new navigation key instance.
                ScreenB(
                    viewModel = viewModel(
                        factory = RouteBViewModel.Factory(key)
                    )
                )
            }
        }
    )
}

@Composable
fun ScreenB(viewModel: RouteBViewModel = viewModel()) {
    ContentBlue("Route id: ${viewModel.key.id} ")
}

class RouteBViewModel(
    val key: RouteB
) : ViewModel() {
    class Factory(
        private val key: RouteB,
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: KClass<T>, extras: CreationExtras): T {
            return RouteBViewModel(key) as T
        }
    }
}