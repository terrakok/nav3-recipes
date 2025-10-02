package org.company.app

import androidx.compose.runtime.Composable
import androidx.compose.runtime.saveable.rememberSerializable
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.serialization.NavBackStackSerializer
import androidx.savedstate.serialization.SavedStateConfiguration
import kotlinx.serialization.PolymorphicSerializer
import org.company.app.theme.AppTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@Preview
@Composable
internal fun App() = AppTheme {
    RecipePicker()
}


@Composable
inline fun <reified T : NavKey> rememberNavBackStackFix(
    configuration: SavedStateConfiguration,
    vararg elements: T,
): NavBackStack<NavKey> {
    return rememberSerializable(
        configuration = configuration,
        serializer = NavBackStackSerializer(PolymorphicSerializer(NavKey::class)),
    ) {
        NavBackStack(*elements)
    }
}