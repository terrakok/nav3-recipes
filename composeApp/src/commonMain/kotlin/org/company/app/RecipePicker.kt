package org.company.app

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.ui.NavDisplay
import org.company.app.animations.AnimatedCase
import org.company.app.basic.BasicCase
import org.company.app.basicdsl.BasicDslCase
import org.company.app.basicsaveable.BasicSaveableCase
import org.company.app.commonui.CommonUiCase
import org.company.app.conditional.ConditionalCase
import org.company.app.dialog.DialogCase
import org.company.app.passingarguments.basicviewmodels.BasicViewModelsCase
import org.company.app.scenes.materiallistdetail.MaterialListDetailCase
import org.company.app.scenes.twopane.TwoPaneCase

private class Recipe(
    val name: String,
    val ui: @Composable () -> Unit
)

private class Heading(val name: String)

private val recipes = listOf(
    Heading("Basic API recipes"),
    Recipe("Basic") { BasicCase() },
    Recipe("Basic DSL") { BasicDslCase() },
    Recipe("Basic Saveable") { BasicSaveableCase() },

    Heading("Layouts and animations"),
    Recipe("Material list-detail layout") { MaterialListDetailCase() }, //TODO
    Recipe("Dialog") { DialogCase() },
    Recipe("Two pane layout (custom scene)") { TwoPaneCase() }, //TODO
    Recipe("Animations") { AnimatedCase() },

    Heading("Common use cases"),
    Recipe("Common UI") { CommonUiCase() },
    Recipe("Conditional navigation") { ConditionalCase() },

//    Heading("Architecture"),
//    Recipe("Modular Navigation") { BasicCase() ModularCase() },

    Heading("Passing navigation arguments"),
    Recipe("Argument passing to basic ViewModel") { BasicViewModelsCase() },
//    Recipe("Argument passing to injected ViewModel") { InjectedViewModelsCase() },
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecipePicker() {
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
        }) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = innerPadding.calculateTopPadding())
        ) {

            val backStack = remember { mutableStateListOf<Any>("root") }
            NavDisplay(
                backStack = backStack,
                onBack = { backStack.removeLastOrNull() },
                entryProvider = { key ->
                    when (key) {
                        "root" -> NavEntry(key) {
                            RecipeList {
                                backStack.add(it)
                            }
                        }

                        is Recipe -> NavEntry(key, contentKey = key.name) {
                            key.ui()
                        }

                        else -> {
                            error("Unknown route: $key")
                        }
                    }
                }
            )
        }
    }
}

@Composable
private fun RecipeList(onClick: (Recipe) -> Unit) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(recipes) { item ->
            when(item){
                is Recipe -> {
                    ListItem(
                        headlineContent = { Text(item.name) },
                        modifier = Modifier.clickable {
                            onClick(item)
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
