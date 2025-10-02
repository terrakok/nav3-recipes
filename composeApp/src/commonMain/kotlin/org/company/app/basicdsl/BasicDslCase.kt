package org.company.app.basicdsl

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.saveable.rememberSerializable
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.serialization.NavBackStackSerializer
import androidx.navigation3.ui.NavDisplay
import androidx.savedstate.serialization.SavedStateConfiguration
import kotlinx.serialization.PolymorphicSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import org.company.app.ContentBlue
import org.company.app.ContentGreen
import org.company.app.rememberNavBackStackFix


/**
 * Basic example with two screens that uses the entryProvider DSL and has a persistent back stack.
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
fun BasicDslCase() {
    val backStack = rememberNavBackStackFix(config, RouteA)
    NavDisplay(
        backStack = backStack,
        onBack = { backStack.removeLastOrNull() },
        entryProvider = entryProvider {
            entry<RouteA> {
                ContentGreen("Welcome to Nav3") {
                    Button(onClick = {
                        backStack.add(RouteB("123"))
                    }) {
                        Text("Click to navigate")
                    }
                }
            }
            entry<RouteB> { key ->
                ContentBlue("Route id: ${key.id} ")
            }
        }
    )
}
