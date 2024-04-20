import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.Modifier.Companion
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyShortcut
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.window.MenuBar
import androidx.compose.ui.window.Tray
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import dev.datlag.kcef.KCEF
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import kotlin.math.max

fun main1() = application {
	val icon = painterResource("drawable/icon.png")

	Tray(
		icon = icon,
		menu = {
			Item("Quit", onClick = ::exitApplication)
		}
	)

	// System.loadLibrary("vlcj") // How to set this up in gradle for distribution on AppStore?

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

// For JCEF (Java Chromium Embedded Framework) web browser
fun main() = application {
	val icon = painterResource("drawable/icon.png")

	Window(
		onCloseRequest = ::exitApplication,
		title = "Cloud Cover USA 2",
		icon = icon
	) {

		var restartRequired by remember { mutableStateOf(false) }
		var downloading by remember { mutableStateOf(0) }
		var initialized by remember { mutableStateOf(false) }

		LaunchedEffect(Unit) {
			withContext(Dispatchers.IO) {
				KCEF.init(builder = {
					installDir(File("kcef-bundle"))
					progress {
						onDownloading {
							downloading = max(it, 0).toInt()
						}
						onInitialized {
							initialized = true
						}
					}
					settings {
						cachePath = File("cache").absolutePath
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
						text = "Loading $downloading%",
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