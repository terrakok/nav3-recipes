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

package com.example.nav3recipes.dialogscenedecorator

import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.lifecycle.compose.dropUnlessResumed
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import androidx.savedstate.serialization.SavedStateConfiguration
import com.example.nav3recipes.content.ContentBlue
import com.example.nav3recipes.content.ContentGreen
import com.example.nav3recipes.content.ContentYellow
import com.example.nav3recipes.scenes.listdetail.ListDetailScene
import com.example.nav3recipes.scenes.listdetail.rememberListDetailSceneStrategy
import kotlinx.serialization.Serializable
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic

@Serializable
private data object Main : NavKey

@Serializable
private data object SettingsList : NavKey

@Serializable
private data object SettingsDetail : NavKey


private val config = SavedStateConfiguration {
    serializersModule = SerializersModule {
        polymorphic(NavKey::class) {
            subclass(Main::class, Main.serializer())
            subclass(SettingsList::class, SettingsList.serializer())
            subclass(SettingsDetail::class, SettingsDetail.serializer())
        }
    }
}

@Composable
fun DialogSceneDecoratorActivity() {
    val backStack = rememberNavBackStack(config, Main)
    val listDetailSceneStrategy = rememberListDetailSceneStrategy<NavKey>()

    val dialogSceneDecoratorStrategy = rememberDialogSceneDecoratorStrategy<NavKey>(
        onDismissAll = { entriesToDismiss ->
            // Caution: This relies on the default behavior of NavEntry using key.toString()
            // to define its contentKey property.
            entriesToDismiss.forEach { entry ->
                backStack
                    .indexOfLast { it.toString() == entry.contentKey }
                    .takeIf { it >= 0 }
                    ?.let { backStack.removeAt(it) }
            }
        }
    )

    SharedTransitionLayout {
        NavDisplay(
            backStack = backStack,
            onBack = { backStack.removeLastOrNull() },
            sceneStrategies = listOf(listDetailSceneStrategy),
            sceneDecoratorStrategies = listOf(dialogSceneDecoratorStrategy),
            sharedTransitionScope = this,
            entryProvider = entryProvider {
                entry<Main> {
                    ContentGreen("Welcome to Nav3") {
                        Button(onClick = dropUnlessResumed {
                            backStack.add(SettingsList)
                        }) {
                            Text("Click to open settings")
                        }
                    }
                }
                entry<SettingsList>(
                    metadata = DialogSceneDecoratorStrategy.sceneDialog(
                        DialogDecoratorSceneConfiguration(
                            backDismissalBehavior = DismissalBehavior.Single
                        )
                    ) + ListDetailScene.listPane()
                ) {
                    ContentBlue(
                        title = "Settings List",
                    ) {
                        Button(onClick = dropUnlessResumed {
                            if (backStack.last() !is SettingsDetail) {
                                backStack.add(SettingsDetail)
                            }
                        }) {
                            Text("Open detail")
                        }
                    }
                }
                entry<SettingsDetail>(
                    metadata = ListDetailScene.detailPane()
                ) {
                    ContentYellow("Settings Detail")
                }
            }
        )
    }
}
