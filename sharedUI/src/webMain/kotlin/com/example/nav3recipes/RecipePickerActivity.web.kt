package com.example.nav3recipes

import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.github.terrakok.navigation3.browser.HierarchicalBrowserNavigation
import com.github.terrakok.navigation3.browser.buildBrowserHistoryFragment

@Composable
internal actual fun BrowserIntegration(backStack: SnapshotStateList<Recipe>) {
    HierarchicalBrowserNavigation {
        backStack.lastOrNull()?.let { key ->
            buildBrowserHistoryFragment(
                name = key.name.lowercase().replace(" ", "_")
            )
        }
    }
}