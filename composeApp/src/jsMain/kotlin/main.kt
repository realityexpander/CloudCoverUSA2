import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.CanvasBasedWindow
import org.jetbrains.skiko.wasm.onWasmReady

fun main() {
    onWasmReady {
        val title = "Cloud Cover USA 2"
        @OptIn(ExperimentalComposeUiApi::class)
        CanvasBasedWindow(title, canvasElementId = "ComposeTarget") {
            App()
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
actual fun getScreenWidth(): Dp = LocalWindowInfo.current
    .containerSize
    .width
    .dp

@OptIn(ExperimentalComposeUiApi::class)
@Composable
actual fun getScreenHeight(): Dp = LocalWindowInfo.current
    .containerSize
    .height
    .dp

@Composable
actual fun VideoPlayer(
    modifier: Modifier,
    url: String,
    onSetupComplete: () -> Unit,
    onCloseVideoWindow: () -> Unit
) {
    Text("Video Player not implemented on this platform")

    LaunchedEffect(Unit) {
        onSetupComplete()
    }
}
