package org.company.app.conditional

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import org.company.app.ContentBlue
import org.company.app.ContentGreen
import org.company.app.ContentYellow

/**
 * Conditional navigation recipe.
 *
 * In this app we have a destination, Profile, that can only be
 * accessed once the user has logged in. If they attempt to access the Profile destination when
 * they're not logged in, they are redirected to the Login destination. Their ultimate destination,
 * Profile, is saved. Once they're logged in, they're taken to the Profile destination.
 *
 */

private data object Home

// A marker interface is used to mark any routes that require login
private data object Profile : AppBackStack.RequiresLogin
private data object Login

@Composable
fun ConditionalCase() {
    val appBackStack = remember {
        AppBackStack(startRoute = Home, loginRoute = Login)
    }

    NavDisplay(
        backStack = appBackStack.backStack,
        onBack = { appBackStack.remove() },
        entryProvider = entryProvider {
            entry<Home> {
                ContentGreen("Welcome to Nav3. Logged in? ${appBackStack.isLoggedIn}") {
                    Column {
                        Button(onClick = { appBackStack.add(Profile) }) {
                            Text("Profile")
                        }
                        Button(onClick = { appBackStack.add(Login) }) {
                            Text("Login")
                        }
                    }
                }
            }
            entry<Profile> {
                ContentBlue("Profile screen (only accessible once logged in)") {
                    Button(onClick = {
                        appBackStack.logout()
                    }) {
                        Text("Logout")
                    }
                }
            }
            entry<Login> {
                ContentYellow("Login screen. Logged in? ${appBackStack.isLoggedIn}") {
                    Button(onClick = {
                        appBackStack.login()
                    }) {
                        Text("Login")
                    }
                }
            }
        }
    )
}

/**
 * A back stack that can handle routes that require login.
 *
 * @param startRoute The starting route
 * @param loginRoute The route that users should be taken to when they attempt to access a route
 * that requires login
 */
class AppBackStack<T : Any>(startRoute: T, private val loginRoute: T) {

    interface RequiresLogin

    private var onLoginSuccessRoute: T? = null

    var isLoggedIn by mutableStateOf(false)
        private set

    val backStack = mutableStateListOf(startRoute)

    fun add(route: T) {
        if (route is RequiresLogin && !isLoggedIn) {
            // Store the intended destination and redirect to login
            onLoginSuccessRoute = route
            backStack.add(loginRoute)
        } else {
            backStack.add(route)
        }

        // If the user explicitly requested the login route, don't redirect them after login
        if (route == loginRoute) {
            onLoginSuccessRoute = null
        }
    }

    fun remove() = backStack.removeLastOrNull()

    fun login() {
        isLoggedIn = true

        onLoginSuccessRoute?.let {
            backStack.add(it)
            backStack.remove(loginRoute)
        }
    }

    fun logout() {
        isLoggedIn = false
        backStack.removeAll { it is RequiresLogin }
    }
}