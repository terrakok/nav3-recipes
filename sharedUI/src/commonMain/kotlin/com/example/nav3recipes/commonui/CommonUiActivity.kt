/*
 * Copyright 2025 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.nav3recipes.commonui

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import com.example.nav3recipes.content.ContentBlue
import com.example.nav3recipes.content.ContentGreen
import com.example.nav3recipes.content.ContentPurple
import com.example.nav3recipes.content.ContentRed
import nav3play.sharedui.generated.resources.Res
import nav3play.sharedui.generated.resources.face
import nav3play.sharedui.generated.resources.home
import nav3play.sharedui.generated.resources.play_arrow
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.vectorResource

private sealed interface TopLevelRoute {
    val icon: DrawableResource
}

private data object Home : TopLevelRoute {
    override val icon = Res.drawable.home
}

private data object ChatList : TopLevelRoute {
    override val icon = Res.drawable.face
}

private data object ChatDetail
private data object Camera : TopLevelRoute {
    override val icon = Res.drawable.play_arrow
}

private val TOP_LEVEL_ROUTES: List<TopLevelRoute> = listOf(Home, ChatList, Camera)

@Composable
fun CommonUiActivity() {
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
                entry<Home> {
                    ContentRed("Home screen")
                }
                entry<ChatList> {
                    ContentGreen("Chat list screen") {
                        Button(onClick = { topLevelBackStack.add(ChatDetail) }) {
                            Text("Go to conversation")
                        }
                    }
                }
                entry<ChatDetail> {
                    ContentBlue("Chat detail screen")
                }
                entry<Camera> {
                    ContentPurple("Camera screen")
                }
            },
        )
    }
}

class TopLevelBackStack<T : Any>(startKey: T) {

    // Maintain a stack for each top level route
    private var topLevelStacks: LinkedHashMap<T, SnapshotStateList<T>> = linkedMapOf(
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

    fun addTopLevel(key: T) {

        // If the top level doesn't exist, add it
        if (topLevelStacks[key] == null) {
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

    fun add(key: T) {
        topLevelStacks[topLevelKey]?.add(key)
        updateBackStack()
    }

    fun removeLast() {
        val removedKey = topLevelStacks[topLevelKey]?.removeLastOrNull()
        // If the removed key was a top level key, remove the associated top level stack
        topLevelStacks.remove(removedKey)
        topLevelKey = topLevelStacks.keys.last()
        updateBackStack()
    }
}

