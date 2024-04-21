import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.multiplatform.webview.web.WebView
import com.multiplatform.webview.web.rememberWebViewState

// Using Webview
@Composable
fun VideoPlayerWebViewImpl(
	url: String,
	modifier: Modifier,
	onSetupComplete: () -> Unit = {},
	onHideProgress: () -> Unit = {},
	isVideoPlayerPaused: Boolean,
) {
//	val webViewState = rememberWebViewState("https://www.ssec.wisc.edu/data/us_comp/movie")
//	val webViewState = rememberWebViewState("https://www.youtube.com/watch?v=Vh9eQ_8_9Hw")
//	val webViewState = rememberWebViewState("https://realityexpander.github.io/CloudCoverUSA2/ssec.html")
	val webViewState = rememberWebViewState(url)
	Column(Modifier.fillMaxSize()) {
		val text = webViewState.let {
			"${it.pageTitle ?: ""} ${it.loadingState} ${it.lastLoadedUrl ?: ""}"
		}
		Text(text, color= Color.White)
		WebView(
			state = webViewState,
			modifier = Modifier.fillMaxSize(),
			onCreated = {
				onSetupComplete()
				onHideProgress()
			},
		)
	}
}