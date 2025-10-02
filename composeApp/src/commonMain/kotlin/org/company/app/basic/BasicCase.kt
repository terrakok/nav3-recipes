package org.company.app.basic

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.ui.NavDisplay
import org.company.app.ContentBlue
import org.company.app.ContentGreen

/**
 * Basic example with two screens, showing how to use the Navigation 3 API.
 */

private data object RouteA

private data class RouteB(val id: String)

@Composable
fun BasicCase() {
    val backStack = remember { mutableStateListOf<Any>(RouteA) }

    NavDisplay(
        backStack = backStack,
        onBack = { backStack.removeLastOrNull() },
        entryProvider = { key ->
            when (key) {
                is RouteA -> NavEntry(key) {
                    ContentGreen("Welcome to Nav3") {
                        Button(onClick = {
                            backStack.add(RouteB("123"))
                        }) {
                            Text("Click to navigate")
                        }
                    }
                }

                is RouteB -> NavEntry(key) {
                    ContentBlue("Route id: ${key.id} ")
                }

                else -> {
                    error("Unknown route: $key")
                }
            }
        }
    )
}