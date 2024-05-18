import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.java.Java
import kotlinx.coroutines.delay

class JVMPlatform : Platform {
	override val name: String = "Java ${System.getProperty("java.version")}"
}

actual fun getPlatform(): Platform = JVMPlatform()

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
	var isProgressVisible by remember { mutableStateOf(true) }
	var isVideoPlayerVisible by remember { mutableStateOf(false) }
	var isVideoPlayerPaused by remember { mutableStateOf(false) }

	LaunchedEffect(Unit) {
		delay(250)
		isVideoPlayerVisible = true
	}

	Column(
		modifier = modifier
			.fillMaxWidth()
			.background(color = androidx.compose.ui.graphics.Color.Black),
		horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally
	) {
		if (isProgressVisible) {
			Spacer(modifier = Modifier.size(50.dp))
			CircularProgressIndicator(
				modifier = Modifier
					.size(50.dp)
			)
		} else {
			Spacer(modifier = Modifier.size(50.dp))
			Button(
				onClick = {
					isVideoPlayerPaused = !isVideoPlayerPaused
				}
			) {
				Text("Play/Pause video")
			}
		}

		if (isVideoPlayerVisible) {
			if(implementation == VideoPlayerImplementation.JCEFWebView) {
				VideoPlayerWebViewImpl(
					url = "https://realityexpander.github.io/CloudCoverUSA2/ssec.html",
					modifier = modifier,
					onSetupComplete = onSetupComplete,
					onHideProgress = {
						isProgressVisible = false
					},
					isVideoPlayerPaused = isVideoPlayerPaused
				)
			} else {
				VideoPlayerVLCJImpl(
					url = url,
					modifier = modifier,
					onSetupComplete = onSetupComplete,
					onHideProgress = {
						isProgressVisible = false
					},
					isVideoPlayerPaused = isVideoPlayerPaused
				)
			}

		}
	}
}

actual val httpEngine: HttpClientEngine
	//    get() = OkHttp.create()
	get() = Java.create()
