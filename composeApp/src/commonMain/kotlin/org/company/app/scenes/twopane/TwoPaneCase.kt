package org.company.app.scenes.twopane

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.navEntryDecorator
import androidx.navigation3.runtime.rememberSavedStateNavEntryDecorator
import androidx.navigation3.scene.Scene
import androidx.navigation3.scene.SceneStrategy
import androidx.navigation3.scene.rememberSceneSetupNavEntryDecorator
import androidx.navigation3.ui.LocalNavAnimatedContentScope
import androidx.navigation3.ui.NavDisplay
import androidx.savedstate.serialization.SavedStateConfiguration
import kotlinx.serialization.Serializable
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import org.company.app.ContentBase
import org.company.app.ContentGreen
import org.company.app.ContentRed
import org.company.app.colors
import org.company.app.rememberNavBackStackFix


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
    val localNavSharedTransitionScope: ProvidableCompositionLocal<SharedTransitionScope> =
        compositionLocalOf {
            throw IllegalStateException(
                "Unexpected access to LocalNavSharedTransitionScope. You must provide a " +
                        "SharedTransitionScope from a call to SharedTransitionLayout() or " +
                        "SharedTransitionScope()"
            )
        }

    /**
     * A [NavEntryDecorator] that wraps each entry in a shared element that is controlled by the
     * [Scene].
     */
    val sharedEntryInSceneNavEntryDecorator = navEntryDecorator<NavKey> { entry ->
        with(localNavSharedTransitionScope.current) {
            Box(
                Modifier.sharedElement(
                    rememberSharedContentState(entry.contentKey),
                    animatedVisibilityScope = LocalNavAnimatedContentScope.current,
                ),
            ) {
                entry.Content()
            }
        }
    }


    val backStack = rememberNavBackStackFix(config, Home)
    val twoPaneStrategy = remember { TwoPaneSceneStrategy<NavKey>() }

    SharedTransitionLayout {
        CompositionLocalProvider(localNavSharedTransitionScope provides this) {
            NavDisplay(
                backStack = backStack,
                onBack = { keysToRemove -> repeat(keysToRemove) { backStack.removeLastOrNull() } },
                entryDecorators = listOf(
                    sharedEntryInSceneNavEntryDecorator,
                    rememberSceneSetupNavEntryDecorator(),
                    rememberSavedStateNavEntryDecorator()
                ),
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
    }
}

private fun NavBackStack<NavKey>.addProductRoute(productId: Int) {
    val productRoute =
        Product(productId)
    // Avoid adding the same product route to the back stack twice.
    if (!contains(productRoute)) {
        add(productRoute)
    }
}
