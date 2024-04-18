import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyShortcut
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.window.MenuBar
import androidx.compose.ui.window.Tray
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

fun main() = application {
	val icon = painterResource("drawable/icon.png")

	Tray(
		icon = icon,
		menu = {
			Item("Quit", onClick = ::exitApplication)
		}
	)

	Window(
		onCloseRequest = ::exitApplication,
		title = "Cloud Cover USA 2",
		icon = icon
	) {
		MenuBar {
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
		}

		App()
	}
}
