package org.company.app.scenes.materiallistdetail

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.adaptive.layout.calculatePaneScaffoldDirective
import androidx.compose.material3.adaptive.navigation3.ListDetailSceneStrategy
import androidx.compose.material3.adaptive.navigation3.rememberListDetailSceneStrategy
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import androidx.savedstate.serialization.SavedStateConfiguration
import kotlinx.serialization.Serializable
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import org.company.app.ContentBlue
import org.company.app.ContentGreen
import org.company.app.ContentRed
import org.company.app.ContentYellow

/**
 * This example uses the Material ListDetailSceneStrategy to create an adaptive scene. It has three
 * destinations: ConversationList, ConversationDetail and Profile. When the window width allows it,
 * the content for these destinations will be shown in a two pane layout.
 */
    @Serializable
    private object ConversationList : NavKey

    @Serializable
    private data class ConversationDetail(val id: String) : NavKey

    @Serializable
    private data object Profile : NavKey

private val config = SavedStateConfiguration {
    serializersModule = SerializersModule {
        polymorphic(NavKey::class) {
            subclass(ConversationList::class, ConversationList.serializer())
            subclass(ConversationDetail::class, ConversationDetail.serializer())
            subclass(Profile::class, Profile.serializer())
        }
    }
}

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun MaterialListDetailCase() {
    val backStack = rememberNavBackStack(config, ConversationList)

    // Override the defaults so that there isn't a horizontal space between the panes.
    // See b/418201867
    val windowAdaptiveInfo = currentWindowAdaptiveInfo()
    val directive = remember(windowAdaptiveInfo) {
        calculatePaneScaffoldDirective(windowAdaptiveInfo)
            .copy(horizontalPartitionSpacerSize = 0.dp)
    }
    val listDetailStrategy = rememberListDetailSceneStrategy<NavKey>(directive = directive)


    NavDisplay(
        backStack = backStack,
        onBack = { backStack.removeLastOrNull() },
        sceneStrategy = listDetailStrategy,
        entryProvider = entryProvider {
            entry<ConversationList>(
                metadata = ListDetailSceneStrategy.listPane(
                    detailPlaceholder = {
                        ContentYellow("Choose a conversation from the list")
                    }
                )
            ) {
                ContentRed("Welcome to Nav3") {
                    Button(onClick = {
                        backStack.add(ConversationDetail("ABC"))
                    }) {
                        Text("View conversation")
                    }
                }
            }
            entry<ConversationDetail>(
                metadata = ListDetailSceneStrategy.detailPane()
            ) { conversation ->
                ContentBlue("Conversation ${conversation.id} ") {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Button(onClick = {
                            backStack.add(Profile)
                        }) {
                            Text("View profile")
                        }
                    }
                }
            }
            entry<Profile>(
                metadata = ListDetailSceneStrategy.extraPane()
            ) {
                ContentGreen("Profile")
            }
        }
    )
}