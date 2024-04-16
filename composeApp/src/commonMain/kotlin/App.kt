import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.unit.coerceIn
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import androidx.compose.ui.unit.min
import coil3.annotation.ExperimentalCoilApi
import coil3.compose.AsyncImage
import coil3.compose.LocalPlatformContext
import coil3.request.ImageRequest
import coil3.request.crossfade
import kotlinx.coroutines.delay
import org.jetbrains.compose.ui.tooling.preview.Preview
import kotlin.math.max
import kotlin.time.Duration.Companion.milliseconds


@OptIn(ExperimentalCoilApi::class)
@Composable
@Preview
fun App() {
    MaterialTheme {
        var showLog by remember { mutableStateOf(false) }
        var loadingLog by remember { mutableStateOf("Idle.") }
        var finishedCount by remember { mutableStateOf(0) }
        val localContext = LocalPlatformContext.current
        var imageWidth by remember { mutableStateOf(0f) }
        var imageHeight by remember { mutableStateOf(0f) }
        var isFirstFrame by remember { mutableStateOf(true) }
        val localScreenWidth = getScreenWidth()
        val localScreenHeight = getScreenHeight()
        var contentWidth by remember { mutableStateOf(0) }
        var contentHeight by remember { mutableStateOf(0) }
        var isLoadingFinished by remember { mutableStateOf(false) }
        var is5DayMovieVisible by remember { mutableStateOf(false) }

        // check orientation
        var isLandscape by remember(localScreenWidth, localScreenHeight) {
            mutableStateOf(localScreenWidth > localScreenHeight)
        }

        val kMaxZoomInFactor = 5f
        var scale by remember { mutableStateOf(1f) }
        var offset by remember {
            mutableStateOf(Offset.Zero)
        }
        var currentFrame by remember { mutableStateOf(0) }
        val imageRequests = remember {
            mutableListOf<Any>().apply {
                repeat(9) { count ->
                    val ir = ImageRequest.Builder(localContext)
                        .data("https://www.ssec.wisc.edu/data/us_comp/image${count}.jpg")
                        .crossfade(true) // prevents flicker
                        .listener(
                            onSuccess = { request, response ->
                                if (finishedCount <= 7) {
                                    loadingLog =
                                        "$loadingLog\nImage $count loaded, ${response.diskCacheKey}."
                                    finishedCount++
                                    imageWidth = response.image.width.toFloat()
                                    imageHeight = response.image.height.toFloat()
                                }
                            },
                            onError = { request, throwable ->
                                loadingLog =
                                    "$loadingLog\nImage $count failed to load, ${throwable.throwable.message}."
                            },
                            onCancel = { request ->
                                loadingLog =
                                    "$loadingLog\nImage $count cancelled, ${request.memoryCacheKey}, ${request.diskCacheKey}."
                            }
                        )
                        .build()
                    add(ir)
                }
            }
        }

        // Load 1 at a time for ios
        LaunchedEffect(finishedCount) {
            currentFrame = (currentFrame + 1) % (imageRequests.size - 1)
            if(finishedCount == 8) {
                isLoadingFinished = true
            }
        }
        // Render the animation
        LaunchedEffect(isLoadingFinished) {
            if(!isLoadingFinished) return@LaunchedEffect

            while (true) {
                if (currentFrame == 0)
                    delay(500.milliseconds)
                else
                    delay(100.milliseconds)

                currentFrame = (currentFrame + 1) % (imageRequests.size - 1)
            }
        }

        // Change scale and offset when content size changes (e.g. rotation)
        LaunchedEffect(contentWidth, contentHeight) {
            if (contentWidth == 0 || contentHeight == 0) return@LaunchedEffect
            if (imageWidth == 0f || imageHeight == 0f) return@LaunchedEffect

            isLandscape = contentWidth > contentHeight
            scale = max(contentHeight / imageHeight, contentWidth / imageWidth)
            offset = Offset.Zero
        }

        BoxWithConstraints(
            modifier = Modifier.fillMaxSize()
        ) {
            // Setup pan/zoom (not rotation) transformable state
            @Suppress("UNUSED_ANONYMOUS_PARAMETER") // for rotationChange
            val state =
                rememberTransformableState { zoomChange, panChange, rotationChange ->
                    scale = (scale * zoomChange).coerceIn(1f, kMaxZoomInFactor)

                    val extraWidth = (scale - 1) * constraints.maxWidth
                    val extraHeight = (scale - 1) * constraints.maxHeight

                    val maxX = extraWidth / 2
                    val maxY = extraHeight / 2

                    offset = Offset(
                        x = (offset.x + scale * panChange.x).coerceIn(-maxX, maxX),
                        y = (offset.y + scale * panChange.y).coerceIn(-maxY, maxY)
                    )
                }

            Column(
                Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                AsyncImage(
                    imageRequests[currentFrame],
                    contentDescription = "image",
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black)
                        .graphicsLayer {
                            scaleX = scale
                            scaleY = scale
                            translationX = offset.x
                            translationY = offset.y
                            this.rotationZ = rotationZ  // allow rotation?
                        }
                        .transformable(
                            state,
                            lockRotationOnZoomPan = true
                        )
                        .onGloballyPositioned {
                            contentWidth = it.size.width
                            contentHeight = it.size.height

                            if (isFirstFrame && contentWidth > 0 && imageWidth > 0) {
                                // Set correct scale for a given image and screen size
                                scale = max(contentHeight / imageHeight, contentWidth / imageWidth)

                                isFirstFrame = false
                            }
                        },
                    contentScale = ContentScale.None,
                )
            }

            Column(
                Modifier.fillMaxHeight(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Button(
                    onClick = {
                        showLog = !showLog
                    }
                ) {
                    Text("Show Log")
                }
                Button(
                    onClick = {
                        is5DayMovieVisible = true
                    }
                ) {
                    Text("Show 5 Day Movie")
                }

                if (showLog) {
                    Text(
                        loadingLog,
                        color = Color.White
                    )
                }

                if (finishedCount < 8) {
                    LinearProgressIndicator(
                        progress = finishedCount / 7f,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                Text(
                    text = "isLandscape: $isLandscape",
                    color = Color.White
                )
            }

            var isLoadingMovie by remember { mutableStateOf(true) }
            if (is5DayMovieVisible) {
                Box(
                    Modifier.fillMaxSize(),
//                        contentAlignment = Alignment.Center
                ) {
                    VideoPlayer(
                        modifier = Modifier
                            .fillMaxWidth(),
//                                .height(300.dp),
                        url = "https://www.ssec.wisc.edu/data/us_comp/us_comp_large.mp4",
//                        url = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4"
                        onSetupComplete = {
                            isLoadingMovie = false
                        }
                    )
                    Button(
                        onClick = {
                            is5DayMovieVisible = false
                            isLoadingMovie = true
                        }
                    ) {
                        Text("Hide 5 Day Movie")
                    }
                    if(isLoadingMovie) {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            CircularProgressIndicator(
                                modifier = Modifier
                                    .height(100.dp)
                            )
                        }
                    }
                }
            }

        }
    }
}

