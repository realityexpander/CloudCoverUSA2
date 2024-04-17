import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyShortcut
import androidx.compose.ui.window.MenuBar
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

fun main() = application {
	Window(
		onCloseRequest = ::exitApplication,
		title = "Cloud Cover USA 2"
	) {
		MenuBar {
			Menu("File") {
				Item("New") {
					println("New file")
				}
				Item("Open") {
					println("Open file")
				}
				Item("Save") {
					println("Save file")
				}
				Item(
					"Quit",
					onClick = ::exitApplication,
					shortcut = KeyShortcut(Key.Q, meta = true)
				)
			}
		}

		App()
	}
}
