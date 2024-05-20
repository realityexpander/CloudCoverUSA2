import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asComposeImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import coil3.Image
import coil3.annotation.ExperimentalCoilApi
import coil3.request.ImageRequest
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

@OptIn(ExperimentalCoilApi::class)
@Composable
expect fun PlatformImage(
	modifier: Modifier,
	coil3Image: Image?, // All platforms except Android (JS renders hi-res)
	coil3ImageRequest: ImageRequest?,  // All platforms including Android (Note: JS uses low-res images)
	contentScale: ContentScale,
	contentDescription: String?
)
