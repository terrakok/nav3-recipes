import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.window.ComposeUIViewController
import androidx.navigationevent.compose.LocalNavigationEventDispatcherOwner
import org.company.app.App
import platform.UIKit.UIViewController

fun MainViewController(): UIViewController = ComposeUIViewController {
//    val navEventDispatcherOwner = LocalNavigationEventDispatcherOwner.current
//    LaunchedEffect(Unit) {
//        navEventDispatcherOwner?.navigationEventDispatcher?.isEnabled = false
//    }
    App()
}
