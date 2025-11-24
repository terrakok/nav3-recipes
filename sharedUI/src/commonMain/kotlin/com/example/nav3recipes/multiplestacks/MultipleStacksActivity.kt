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

package com.example.nav3recipes.multiplestacks

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import androidx.savedstate.serialization.SavedStateConfiguration
import kotlinx.serialization.Serializable
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import nav3play.sharedui.generated.resources.Res
import nav3play.sharedui.generated.resources.face
import nav3play.sharedui.generated.resources.home
import nav3play.sharedui.generated.resources.play_arrow
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.vectorResource


@Serializable
data object RouteA : NavKey

@Serializable
data object RouteA1 : NavKey

@Serializable
data object RouteB : NavKey

@Serializable
data object RouteB1 : NavKey

@Serializable
data object RouteC : NavKey

@Serializable
data object RouteC1 : NavKey

val config = SavedStateConfiguration {
    serializersModule = SerializersModule {
        polymorphic(NavKey::class) {
            subclass(RouteA::class, RouteA.serializer())
            subclass(RouteA1::class, RouteA1.serializer())
            subclass(RouteB::class, RouteB.serializer())
            subclass(RouteB1::class, RouteB1.serializer())
            subclass(RouteC::class, RouteC.serializer())
            subclass(RouteC1::class, RouteC1.serializer())
        }
    }
}

private val TOP_LEVEL_ROUTES = mapOf<NavKey, NavBarItem>(
    RouteA to NavBarItem(icon = Res.drawable.home, description = "Route A"),
    RouteB to NavBarItem(icon = Res.drawable.face, description = "Route B"),
    RouteC to NavBarItem(icon = Res.drawable.play_arrow, description = "Route C"),
)

data class NavBarItem(
    val icon: DrawableResource,
    val description: String
)

@Composable
fun MultipleStacksActivity() {
    val navigationState = rememberNavigationState(
        startRoute = RouteA,
        topLevelRoutes = TOP_LEVEL_ROUTES.keys
    )

    val navigator = remember { Navigator(navigationState) }

    val entryProvider = entryProvider {
        featureASection(onSubRouteClick = { navigator.navigate(RouteA1) })
        featureBSection(onSubRouteClick = { navigator.navigate(RouteB1) })
        featureCSection(onSubRouteClick = { navigator.navigate(RouteC1) })
    }

    Scaffold(bottomBar = {
        NavigationBar {
            TOP_LEVEL_ROUTES.forEach { (key, value) ->
                val isSelected = key == navigationState.topLevelRoute
                NavigationBarItem(
                    selected = isSelected,
                    onClick = { navigator.navigate(key) },
                    icon = {
                        Icon(
                            imageVector = vectorResource(value.icon),
                            contentDescription = value.description
                        )
                    },
                    label = { Text(value.description) }
                )
            }
        }
    }) { paddingValues ->
        NavDisplay(
            entries = navigationState.toEntries(entryProvider),
            onBack = { navigator.goBack() },
            modifier = Modifier.padding(paddingValues)
        )
    }
}
