import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.uikit.EndEdgeGestureBehavior
import androidx.compose.ui.window.ComposeUIViewController
import org.company.app.App
import platform.UIKit.UIViewController

fun MainViewController(): UIViewController = ComposeUIViewController {
    App()
}
