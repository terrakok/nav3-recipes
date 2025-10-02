package org.company.app.commonui

import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import nav3play.composeapp.generated.resources.Res
import nav3play.composeapp.generated.resources.ic_cyclone
import org.company.app.ContentBlue
import org.company.app.ContentGreen
import org.company.app.ContentPurple
import org.company.app.ContentRed
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.Resource
import org.jetbrains.compose.resources.vectorResource

/**
 * Common navigation UI example. This app has three top level routes: Home, ChatList and Camera.
 * ChatList has a sub-route: ChatDetail.
 *
 * The app back stack state is modeled in `TopLevelBackStack`. This creates a back stack for each
 * top level route. It flattens those maps to create a single back stack for `NavDisplay`. This allows
 * `NavDisplay` to know where to go back to.
 *
 * Note that in this example, the Home route can move above the ChatList and Camera routes, meaning
 * navigating back from Home doesn't necessarily leave the app. The app will exit when the user goes
 * back from a single remaining top level route in the back stack.
 */

private sealed interface TopLevelRoute {
    val icon: DrawableResource
}
private data object Home : TopLevelRoute { override val icon = Res.drawable.ic_cyclone }
private data object ChatList : TopLevelRoute { override val icon = Res.drawable.ic_cyclone }
private data object ChatDetail
private data object Camera : TopLevelRoute { override val icon = Res.drawable.ic_cyclone }

private val TOP_LEVEL_ROUTES : List<TopLevelRoute> = listOf(Home, ChatList, Camera)

@Composable
fun CommonUiCase() {
    val topLevelBackStack = remember { TopLevelBackStack<Any>(Home) }

    Scaffold(
        bottomBar = {
            NavigationBar {
                TOP_LEVEL_ROUTES.forEach { topLevelRoute ->

                    val isSelected = topLevelRoute == topLevelBackStack.topLevelKey
                    NavigationBarItem(
                        selected = isSelected,
                        onClick = {
                            topLevelBackStack.addTopLevel(topLevelRoute)
                        },
                        icon = {
                            Icon(
                                imageVector = vectorResource(topLevelRoute.icon),
                                contentDescription = null
                            )
                        }
                    )
                }
            }
        }
    ) { _ ->
        NavDisplay(
            backStack = topLevelBackStack.backStack,
            onBack = { topLevelBackStack.removeLast() },
            entryProvider = entryProvider {
                entry<Home>{
                    ContentRed("Home screen")
                }
                entry<ChatList>{
                    ContentGreen("Chat list screen"){
                        Button(onClick = { topLevelBackStack.add(ChatDetail) }) {
                            Text("Go to conversation")
                        }
                    }
                }
                entry<ChatDetail>{
                    ContentBlue("Chat detail screen")
                }
                entry<Camera>{
                    ContentPurple("Camera screen")
                }
            },
        )
    }
}

class TopLevelBackStack<T: Any>(startKey: T) {

    // Maintain a stack for each top level route
    private var topLevelStacks : LinkedHashMap<T, SnapshotStateList<T>> = linkedMapOf(
        startKey to mutableStateListOf(startKey)
    )

    // Expose the current top level route for consumers
    var topLevelKey by mutableStateOf(startKey)
        private set

    // Expose the back stack so it can be rendered by the NavDisplay
    val backStack = mutableStateListOf(startKey)

    private fun updateBackStack() =
        backStack.apply {
            clear()
            addAll(topLevelStacks.flatMap { it.value })
        }

    fun addTopLevel(key: T){

        // If the top level doesn't exist, add it
        if (topLevelStacks[key] == null){
            topLevelStacks.put(key, mutableStateListOf(key))
        } else {
            // Otherwise just move it to the end of the stacks
            topLevelStacks.apply {
                remove(key)?.let {
                    put(key, it)
                }
            }
        }
        topLevelKey = key
        updateBackStack()
    }

    fun add(key: T){
        topLevelStacks[topLevelKey]?.add(key)
        updateBackStack()
    }

    fun removeLast(){
        val removedKey = topLevelStacks[topLevelKey]?.removeLastOrNull()
        // If the removed key was a top level key, remove the associated top level stack
        topLevelStacks.remove(removedKey)
        topLevelKey = topLevelStacks.keys.last()
        updateBackStack()
    }
}