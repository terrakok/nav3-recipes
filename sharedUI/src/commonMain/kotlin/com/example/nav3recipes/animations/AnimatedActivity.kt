package com.example.nav3recipes.animations


import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import androidx.savedstate.serialization.SavedStateConfiguration
import com.example.nav3recipes.content.ContentGreen
import com.example.nav3recipes.content.ContentMauve
import com.example.nav3recipes.content.ContentOrange
import kotlinx.serialization.Serializable
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic


@Serializable
private data object ScreenA : NavKey

@Serializable
private data object ScreenB : NavKey

@Serializable
private data object ScreenC : NavKey

private val config = SavedStateConfiguration {
    serializersModule = SerializersModule {
        polymorphic(NavKey::class) {
            subclass(ScreenA::class, ScreenA.serializer())
            subclass(ScreenB::class, ScreenB.serializer())
            subclass(ScreenB::class, ScreenB.serializer())
        }
    }
}

@Composable
fun AnimatedActivity() {

    val backStack = rememberNavBackStack(config, ScreenA)

    NavDisplay(
        backStack = backStack,
        onBack = { backStack.removeLastOrNull() },
        entryProvider = entryProvider {
            entry<ScreenA> {
                ContentOrange("This is Screen A") {
                    Button(onClick = { backStack.add(ScreenB) }) {
                        Text("Go to Screen B")
                    }
                }
            }
            entry<ScreenB> {
                ContentMauve("This is Screen B") {
                    Button(onClick = { backStack.add(ScreenC) }) {
                        Text("Go to Screen C")
                    }
                }
            }
            entry<ScreenC>(
                metadata = NavDisplay.transitionSpec {
                    // Slide new content up, keeping the old content in place underneath
                    slideInVertically(
                        initialOffsetY = { it },
                        animationSpec = tween(1000)
                    ) togetherWith ExitTransition.KeepUntilTransitionsFinished
                } + NavDisplay.popTransitionSpec {
                    // Slide old content down, revealing the new content in place underneath
                    EnterTransition.None togetherWith
                            slideOutVertically(
                                targetOffsetY = { it },
                                animationSpec = tween(1000)
                            )
                } + NavDisplay.predictivePopTransitionSpec {
                    // Slide old content down, revealing the new content in place underneath
                    EnterTransition.None togetherWith
                            slideOutVertically(
                                targetOffsetY = { it },
                                animationSpec = tween(1000)
                            )
                }
            ) {
                ContentGreen("This is Screen C")
            }
        },
        transitionSpec = {
            // Slide in from right when navigating forward
            slideInHorizontally(
                initialOffsetX = { it },
                animationSpec = tween(1000)
            ) togetherWith slideOutHorizontally(
                targetOffsetX = { -it },
                animationSpec = tween(1000)
            )
        },
        popTransitionSpec = {
            // Slide in from left when navigating back
            slideInHorizontally(
                initialOffsetX = { -it },
                animationSpec = tween(1000)
            ) togetherWith slideOutHorizontally(
                targetOffsetX = { it },
                animationSpec = tween(1000)
            )
        },
        predictivePopTransitionSpec = {
            // Slide in from left when navigating back
            slideInHorizontally(
                initialOffsetX = { -it },
                animationSpec = tween(1000)
            ) togetherWith slideOutHorizontally(
                targetOffsetX = { it },
                animationSpec = tween(1000)
            )
        }
    )
}
