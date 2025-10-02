package org.company.app.dialog

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.scene.DialogSceneStrategy
import androidx.navigation3.scene.SceneStrategy
import androidx.navigation3.ui.NavDisplay
import androidx.navigationevent.NavigationEventInfo
import androidx.navigationevent.NavigationEventTransitionState
import androidx.navigationevent.compose.LocalNavigationEventDispatcherOwner
import androidx.navigationevent.compose.NavigationEventHandler
import androidx.navigationevent.compose.rememberNavigationEventState
import androidx.savedstate.serialization.SavedStateConfiguration
import kotlinx.serialization.Serializable
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import org.company.app.ContentBlue
import org.company.app.ContentGreen
import org.company.app.rememberNavBackStackFix

/**
 * This recipe demonstrates how to create a dialog. It does this by:
 *
 * - Adding the `DialogSceneStrategy` to the list of strategies used by `NavDisplay`.
 * - Adding `DialogSceneStrategy.dialog` to a `NavEntry`'s metadata to indicate that it is a
 * dialog. In this case it is applied to the `NavEntry` for `RouteB`.
 *
 * See also https://developer.android.com/guide/navigation/navigation-3/custom-layouts
 */

@Serializable
private data object RouteA : NavKey

@Serializable
private data class RouteB(val id: String) : NavKey

private val config = SavedStateConfiguration {
    serializersModule = SerializersModule {
        polymorphic(NavKey::class) {
            subclass(RouteA::class, RouteA.serializer())
            subclass(RouteB::class, RouteB.serializer())
        }
    }
}

@Composable
fun DialogCase() {
    val backStack = rememberNavBackStackFix(config, RouteA)
    val dialogStrategy = remember { DialogSceneStrategy<NavKey>() }

    NavDisplay(
        backStack = backStack,
        onBack = { backStack.removeLastOrNull() },
        sceneStrategy = dialogStrategy,
        entryProvider = entryProvider {
            entry<RouteA> {
                ContentGreen("Welcome to Nav3") {
                    Button(onClick = {
                        backStack.add(RouteB("123"))
                    }) {
                        Text("Click to open dialog")
                    }
                }
            }
            entry<RouteB>(
                metadata = DialogSceneStrategy.dialog(
                    DialogProperties()//(windowTitle = "Route B dialog")
                )
            ) { key ->
                ContentBlue(
                    title = "Route id: ${key.id}",
                    modifier = Modifier.clip(
                        shape = RoundedCornerShape(16.dp)
                    ),
                    content = {
                        val dispatcher = LocalNavigationEventDispatcherOwner.current?.navigationEventDispatcher!!
                        val state by dispatcher.transitionState.collectAsStateWithLifecycle()

                        Text("Route B content: progress = ${(state as? NavigationEventTransitionState.InProgress)?.latestEvent?.progress ?: 0}")
                    }
                )
            }
        }
    )
}