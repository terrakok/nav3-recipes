package org.company.app.scenes.twopane

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import androidx.savedstate.serialization.SavedStateConfiguration
import kotlinx.serialization.Serializable
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import org.company.app.ContentBase
import org.company.app.ContentGreen
import org.company.app.ContentRed
import org.company.app.colors


/**
 * This example shows how to create custom layouts using the Scenes API.
 *
 * A custom Scene, `TwoPaneScene`, will render content in two panes if:
 *
 * - the window width is over 600dp
 * - the last two nav entries on the back stack have indicated that they support being displayed in
 * a `TwoPaneScene` in their metadata.
 *
 *
 * @see `TwoPaneScene`
 */
@Serializable
private object Home : NavKey
@Serializable
private data class Product(val id: Int) : NavKey
@Serializable
private data object Profile : NavKey

private val config = SavedStateConfiguration {
    serializersModule = SerializersModule {
        polymorphic(NavKey::class) {
            subclass(Home::class, Home.serializer())
            subclass(Product::class, Product.serializer())
            subclass(Profile::class, Profile.serializer())
        }
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun TwoPaneCase() {
    val backStack = rememberNavBackStack(config, Home)
    val twoPaneStrategy = rememberTwoPaneSceneStrategy<NavKey>()

    NavDisplay(
        backStack = backStack,
        onBack = { backStack.removeLastOrNull() },
        sceneStrategy = twoPaneStrategy,
        entryProvider = entryProvider {
            entry<Home>(
                metadata = TwoPaneScene.twoPane()
            ) {
                ContentRed("Welcome to Nav3") {
                    Button(onClick = { backStack.addProductRoute(1) }) {
                        Text("View the first product")
                    }
                }
            }
            entry<Product>(
                metadata = TwoPaneScene.twoPane()
            ) { product ->
                ContentBase(
                    "Product ${product.id} ",
                    Modifier.background(colors[product.id % colors.size])
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Button(onClick = {
                            backStack.addProductRoute(product.id + 1)
                        }) {
                            Text("View the next product")
                        }
                        Button(onClick = {
                            backStack.add(Profile)
                        }) {
                            Text("View profile")
                        }
                    }
                }
            }
            entry<Profile> {
                ContentGreen("Profile (single pane only)")
            }
        }
    )
}

private fun NavBackStack<NavKey>.addProductRoute(productId: Int) {
    val productRoute =
        Product(productId)
    // Avoid adding the same product route to the back stack twice.
    if (!contains(productRoute)) {
        add(productRoute)
    }
}
