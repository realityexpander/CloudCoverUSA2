import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.CanvasBasedWindow
import com.hamama.kwhi.HtmlView
import kotlinx.browser.document
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
	HtmlView(
		modifier = modifier,
		factory = {
			val video = document.createElement(
				"video"
			)
			video.setAttribute(
				"src",
                url,
			)
			video.setAttribute("controls", "true")
			video.setAttribute("controlsList", "nofullscreen")
			video.setAttribute("autoplay", "true")
			video.setAttribute("loop", "true")
			video.addEventListener("loadeddata", {
				onSetupComplete()
			}, false)

			video
		},
		update = {
			it.setAttribute(
				"video",
				url
			)
		}
	)
}