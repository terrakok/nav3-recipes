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

package com.example.nav3recipes

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import com.example.nav3recipes.animations.AnimatedActivity
import com.example.nav3recipes.basic.BasicActivity
import com.example.nav3recipes.basicdsl.BasicDslActivity
import com.example.nav3recipes.basicsaveable.BasicSaveableActivity
import com.example.nav3recipes.bottomsheet.BottomSheetActivity
import com.example.nav3recipes.commonui.CommonUiActivity
import com.example.nav3recipes.conditional.ConditionalActivity
import com.example.nav3recipes.dialog.DialogActivity
import com.example.nav3recipes.material.listdetail.MaterialListDetailActivity
import com.example.nav3recipes.material.supportingpane.MaterialSupportingPaneActivity
import com.example.nav3recipes.multiplestacks.MultipleStacksActivity
import com.example.nav3recipes.passingarguments.viewmodels.basic.BasicViewModelsActivity
import com.example.nav3recipes.results.event.ResultEventActivity
import com.example.nav3recipes.results.state.ResultStateActivity
import com.example.nav3recipes.scenes.listdetail.ListDetailActivity
import com.example.nav3recipes.scenes.twopane.TwoPaneActivity
import com.example.nav3recipes.theme.AppTheme

/**
 * Activity to show all available recipes and allow users to launch each one.
 */
internal class Recipe(
    val name: String,
    val activityFun: @Composable () -> Unit
)

private val Root = Recipe("Root", { })

private class Heading(val name: String)

private val recipes = listOf(
    Heading("Basic API recipes"),
    Recipe("Basic", { BasicActivity() }),
    Recipe("Basic DSL", { BasicDslActivity() }),
    Recipe("Basic Saveable", { BasicSaveableActivity() }),

    Heading("Layouts using Scenes"),
    Recipe("List-detail", { ListDetailActivity() }),
    Recipe("Two pane", { TwoPaneActivity() }),
    Recipe("Bottom Sheet", { BottomSheetActivity() }),
    Recipe("Dialog", { DialogActivity() }),

    Heading("Material adaptive layouts"),
    Recipe("Material list-detail layout", { MaterialListDetailActivity() }),
    Recipe("Material supporting-pane layout", { MaterialSupportingPaneActivity() }),

    Heading("Animations"),
    Recipe("NavDisplay and NavEntry animations", { AnimatedActivity() }),

    Heading("Common use cases"),
    Recipe("Common UI", { CommonUiActivity() }),
    Recipe("Multiple Stacks", { MultipleStacksActivity() }),
    Recipe("Conditional navigation", { ConditionalActivity() }),

//    Heading("Architecture"),
//    Recipe("Hilt - Modular Navigation", { HiltModularActivity() }),
//    Recipe("Koin - Modular Navigation", { KoinModularActivity() }),

    Heading("Passing navigation arguments using ViewModels"),
    Recipe("Basic", { BasicViewModelsActivity() }),
//    Recipe("Using Hilt", { HiltViewModelsActivity() }),
//    Recipe("Using Koin", { KoinViewModelsActivity() }),

    Heading("Returning Results"),
    Recipe("Return result as Event", { ResultEventActivity() }),
    Recipe("Return result as State", { ResultStateActivity() }),

//    Heading("Deeplink"),
//    Recipe("Parse Intent", { CreateDeepLinkActivity() }),
)

@Composable
fun App(
    onThemeChanged: @Composable (isDark: Boolean) -> Unit = {}
) = AppTheme(onThemeChanged) {
    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        RecipePickerActivity()
    }
}

@Composable
internal expect fun BrowserIntegration(backStack: SnapshotStateList<Recipe>)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecipePickerActivity() {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text("Recipes") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        },
        contentWindowInsets = WindowInsets.systemBars.exclude(WindowInsets.navigationBars),
    ) { innerPadding ->
        val backStack = remember { mutableStateListOf(Root) }
        BrowserIntegration(backStack)
        NavDisplay(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .consumeWindowInsets(innerPadding),
            backStack = backStack,
            onBack = { backStack.removeLastOrNull() },
            entryProvider = entryProvider {
                entry<Recipe> {
                    if (it == Root) {
                        RecipeList { recipe -> backStack.add(recipe) }
                    } else {
                        it.activityFun()
                    }
                }
            }
        )
    }
}


@Composable
private fun RecipeList(
    start: (Recipe) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(recipes) { item ->
            when (item) {
                is Recipe -> {
                    ListItem(
                        headlineContent = { Text(item.name) },
                        modifier = Modifier.clickable {
                            start(item)
                        }
                    )
                }

                is Heading -> {
                    ListItem(
                        headlineContent = {
                            Text(
                                text = item.name,
                                fontWeight = FontWeight.Bold
                            )
                        },
                        modifier = Modifier.height(48.dp),
                        colors = ListItemDefaults.colors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant
                        )
                    )
                }
            }
        }
    }
}
