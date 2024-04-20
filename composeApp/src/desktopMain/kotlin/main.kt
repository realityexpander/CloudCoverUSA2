import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.window.Tray
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import dev.datlag.kcef.KCEF
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import kotlin.math.max

// Choose the video player implementation
val implementation: VideoPlayerImplementation = VideoPlayerImplementation.JCEFWebView
sealed class VideoPlayerImplementation {
	data object VLCJ : VideoPlayerImplementation()
	data object JCEFWebView : VideoPlayerImplementation()
}

// For JCEFWebView (Java Chromium Embedded Framework) web browser
fun main() = application {
	val icon = painterResource("drawable/icon.png")
	Tray(
		icon = icon,
		menu = {
			Item("Quit", onClick = ::exitApplication)
		}
	)

	when (implementation) {
		is VideoPlayerImplementation.VLCJ -> {
			Window(
				onCloseRequest = ::exitApplication,
				title = "Cloud Cover USA 2",
				icon = icon
			) {
				//		MenuBar {
				// Menu("File") {
				// 	Item("New") {
				// 		println("New file")
				// 	}
				// 	Item("Open") {
				// 		println("Open file")
				// 	}
				// 	Item("Save") {
				// 		println("Save file")
				// 	}
				// 	Item(
				// 		"Quit",
				// 		onClick = ::exitApplication,
				// 		shortcut = KeyShortcut(Key.X, meta = true)
				// 	)
				// }
				//		}

				App()
			}
		}
		is VideoPlayerImplementation.JCEFWebView -> {
			Window(
				onCloseRequest = ::exitApplication,
				title = "Cloud Cover USA 2",
				icon = icon
			) {

				var restartRequired by remember { mutableStateOf(false) }
				var downloading by remember { mutableStateOf(0F) }
				var initialized by remember { mutableStateOf(false) }

				LaunchedEffect(Unit) {
					withContext(Dispatchers.IO) {
						KCEF.init(builder = {
							installDir(File("Downloads/kcef-bundle"))
							progress {
								onDownloading {
									downloading = max(it, 0F)
								}
								onInitialized {
									initialized = true
								}
							}
							settings {
								cachePath = File("Downloads/cache").absolutePath
							}
						}, onError = {
							it?.printStackTrace()
						}, onRestartRequired = {
							restartRequired = true
						})
					}
				}

				if (restartRequired) {
					Text(text = "Restart required.")
				} else {
					if (initialized) {
						App()
					} else {
						Column(
							modifier = Modifier
								.fillMaxSize()
								.background(Color.Black)
							,
							verticalArrangement = androidx.compose.foundation.layout.Arrangement.Center,
							horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally
						) {
							Text(
								text = "Loading ${downloading.toInt()}%",
								color = Color.White
							)
						}
					}
				}

				DisposableEffect(Unit) {
					onDispose {
						KCEF.disposeBlocking()
					}
				}
			}
		}
	}
}
