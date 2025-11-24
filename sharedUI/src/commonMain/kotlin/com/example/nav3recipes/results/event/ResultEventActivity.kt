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

package com.example.nav3recipes.results.event

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.example.nav3recipes.results.common.*

@Composable
fun ResultEventActivity() {
    val resultBus = remember { ResultEventBus() }

    Scaffold { paddingValues ->

        val backStack = rememberNavBackStack(config, Home)

        NavDisplay(
            backStack = backStack,
            modifier = Modifier.padding(paddingValues),
            onBack = { backStack.removeLastOrNull() },
            entryProvider = entryProvider {
                entry<Home> {
                    val viewModel = viewModel<HomeViewModel>(key = Home.toString()) { HomeViewModel() }
                    ResultEffect<Person>(resultBus) { person ->
                        viewModel.person = person
                    }

                    val person = viewModel.person
                    HomeScreen(
                        person = person,
                        onNext = { backStack.add(PersonDetailsForm()) }
                    )
                }
                entry<PersonDetailsForm> {
                    PersonDetailsScreen(
                        onSubmit = { person ->
                            resultBus.sendResult<Person>(result = person)
                            backStack.removeLastOrNull()
                        }
                    )
                }
            }
        )
    }
}
