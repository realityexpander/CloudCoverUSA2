import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.CanvasBasedWindow
import com.hamama.kwhi.HtmlView
import com.seiko.imageloader.ImageLoader
import com.seiko.imageloader.LocalImageLoader
import com.seiko.imageloader.component.setupDefaultComponents
import com.seiko.imageloader.intercept.bitmapMemoryCacheConfig
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.js.Js
import kotlinx.browser.document
import okio.FileSystem
import okio.fakefilesystem.FakeFileSystem
import org.jetbrains.skiko.wasm.onWasmReady

fun main() {
	onWasmReady {
		val title = "Cloud Cover USA 2"
		@OptIn(ExperimentalComposeUiApi::class)
		CanvasBasedWindow(title, canvasElementId = "ComposeTarget") {
			CompositionLocalProvider(
				LocalImageLoader provides remember { generateImageLoader() },
			) {
				App()
			}
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

private fun generateImageLoader(): ImageLoader {
	return ImageLoader {
		commonConfig()
		components {
			setupDefaultComponents()
		}
		interceptor {
			bitmapMemoryCacheConfig {
				maxSize(32 * 1024 * 1024) // 32MB
			}
			diskCacheConfig(FakeFileSystem().apply { emulateUnix() }) {
				directory(FileSystem.SYSTEM_TEMPORARY_DIRECTORY)
				maxSizeBytes(256L * 1024 * 1024) // 256MB
			}
		}
	}
}

actual val httpEngine: HttpClientEngine
	get() = Js.create()

