import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import io.ktor.client.engine.HttpClientEngine

interface Platform {
	val name: String
}

expect fun getPlatform(): Platform

@Composable
expect fun getScreenWidth(): Dp

@Composable
expect fun getScreenHeight(): Dp

@Composable
expect fun VideoPlayer(
	modifier: Modifier,
	url: String,
	onSetupComplete: () -> Unit = {},
	onCloseVideoWindow: () -> Unit = {}
)

expect val httpEngine: HttpClientEngine
