import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Help
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.CalendarViewWeek
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Today
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.SingletonImageLoader
import coil3.annotation.ExperimentalCoilApi
import coil3.compose.AsyncImage
import coil3.compose.LocalPlatformContext
import coil3.network.NetworkHeaders
import coil3.network.httpHeaders
import coil3.request.ImageRequest
import coil3.request.crossfade
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.jetbrains.compose.ui.tooling.preview.Preview
import kotlin.math.max
import kotlin.time.Duration.Companion.milliseconds

val platform = getPlatform()
const val isDebugModeActive = false

@OptIn(ExperimentalCoilApi::class)
@Composable
@Preview
fun App() {
    MaterialTheme {
        val localContext = LocalPlatformContext.current
        val scope = rememberCoroutineScope()

        var isLoadingFinished by remember { mutableStateOf(false) }
        var is12HrMapVisible by remember { mutableStateOf(false) }
        var isShowAboutInfo by remember { mutableStateOf(false) }
        var isInitialized by remember { mutableStateOf(false) }
        var is5DayMovieVisible by remember { mutableStateOf(false) }
        var isInternetConnectivityWarningVisible by remember { mutableStateOf(false) }

        // Log
        var showLog by remember { mutableStateOf(false) }
        var loadingLog by remember { mutableStateOf("Idle.") }

        // Animation specs
        val numFrames by remember(is12HrMapVisible) {
            mutableStateOf(if (is12HrMapVisible) 25 else 8)
        }
        val rootUrl by remember(is12HrMapVisible) {
            mutableStateOf(
                if (is12HrMapVisible)
                    "https://www.ssec.wisc.edu/data/us_comp/big/image"
//                    "https://wsrv.nl/?url=https://www.ssec.wisc.edu/data/us_comp/big/image" // using proxy for js target
                else
                    "https://www.ssec.wisc.edu/data/us_comp/image"
//                    "https://wsrv.nl/?url=https://www.ssec.wisc.edu/data/us_comp/image" // using proxy for js target
            )
        }
        var isFirstFrame by remember { mutableStateOf(true) }
        var finishedCount by remember { mutableStateOf(0) }
        var imageWidth by remember { mutableStateOf(800f) }
        var imageHeight by remember { mutableStateOf(600f) }
        val localScreenWidth = getScreenWidth()
        val localScreenHeight = getScreenHeight()
        var contentWidth by remember { mutableStateOf(0) }
        var contentHeight by remember { mutableStateOf(0) }
        val imageRequests = remember {
            mutableListOf<ImageRequest>()
        }

        // check orientation
        var isLandscape by remember(localScreenWidth, localScreenHeight) {
            mutableStateOf(localScreenWidth > localScreenHeight)
        }

        val kMaxZoomInFactor = 5f
        var scale by remember { mutableStateOf(1f) }
        var offset by remember { mutableStateOf(Offset.Zero) }
        var currentFrame by remember { mutableStateOf(-1) }

        fun addToLog(msg: String) {
            loadingLog = "$msg\n$loadingLog"
        }

        var shouldReset by remember { mutableStateOf(false) }
        fun resetReloadMap() {
            scale = 1f
            offset = Offset.Zero
            isLoadingFinished = false
            scope.launch {
                SingletonImageLoader.get(localContext).diskCache?.clear()
                SingletonImageLoader.get(localContext).memoryCache?.clear()
                imageRequests.clear()
                isFirstFrame = true
                isInitialized = false
                finishedCount = 0
                currentFrame = -1

                shouldReset = !shouldReset // triggers reset by changing the value
            }
        }

        // Load 1 at a time in sequence due to how Image API works
        LaunchedEffect(finishedCount, shouldReset) {
            if (finishedCount >= numFrames) {
                isLoadingFinished = true
            } else {
                val random = (0..1_000_000).random() // allows for cache busting
                imageRequests.add(
                    ImageRequest.Builder(localContext)
                        .data("$rootUrl$finishedCount.jpg?$random")
//                        .data("https://realityexpander.github.io/CloudCoverUSA2/icon.png") //?" + (0..1_000_000).random()) //
//                        .data("https://wsrv.nl/?url=https://www.ssec.wisc.edu/data/us_comp/image0.jpg")
//                        .data("$rootUrl$finishedCount.jpg&w=300") //?" + (0..1_000_000).random())
//                        .data("https://wsrv.nl/?url=https://plus.unsplash.com/premium_photo-1661438314870-d819b854b58e&w=300")
//                        .data("https://plus.unsplash.com/premium_photo-1661438314870-d819b854b58e")
						.crossfade(true) // prevents flicker
                        .listener(
                            onStart = { request ->
                                // loadingLog =
                                //	"$loadingLog\nImage $finishedCount started loading, finishedCount = ${request.httpHeaders["finishedCount"]}."
                            },
                            onSuccess = { request, response ->
                                if (finishedCount < numFrames) {
                                    addToLog("Image $finishedCount loaded, key=${response.memoryCacheKey}.")
                                    imageWidth = response.image.width.toFloat()
                                    imageHeight = response.image.height.toFloat()

                                    println("Image finishedCount: $finishedCount, numFrames: $numFrames, currentFrame: $currentFrame")
                                    finishedCount++
                                    isInternetConnectivityWarningVisible = false
                                }
                            },
                            onError = { request, throwable ->
                                addToLog("Image ${request.diskCacheKey} failed to load, ${throwable.throwable.message}.")

                                // Restart from 0
                                scope.launch {
                                    isInternetConnectivityWarningVisible = true
                                    delay(1000.milliseconds)
                                    resetReloadMap()
                                }
                            },
                            onCancel = { request ->
                                val frameIdx = request.httpHeaders["finishedCount"]?.toInt()
                                addToLog("Image cancelled, finishedCount = $frameIdx")
                                println("Cancelled, request.httpHeaders frameIdx= $frameIdx")
                            }
                        )
                        // leaves blank frames .diskCachePolicy(CachePolicy.DISABLED) // Always load the latest upon launch
//						flickers .memoryCachePolicy(CachePolicy.DISABLED) // Always load the latest upon launch
                        //
                        //.networkCachePolicy(CachePolicy.DISABLED) // Always load the latest upon launch
                        // brief flicker but plays all frames
//                        .diskCacheKey("$rootUrl$finishedCount.jpg")
                        .diskCacheKey("$rootUrl$finishedCount.jpg?$random")
                        //flicker .memoryCacheKey("$rootUrl$finishedCount.jpg?" + (0..1_000_000).random())
                        .httpHeaders(
                            headers = NetworkHeaders.Builder()
                                .add("finishedCount", finishedCount.toString())
                                .build()
                        )
                        .build()
                )

                isInitialized = true
                currentFrame++
            }
        }

        // Run the animation frames
        LaunchedEffect(isLoadingFinished) {
            if (!isLoadingFinished) return@LaunchedEffect
            if (imageRequests.size == 0) return@LaunchedEffect

            isInternetConnectivityWarningVisible = false

            while (true) {
                if (currentFrame == 0)
                    delay(500.milliseconds)
                else
                    delay(100.milliseconds)

                if (!isLoadingFinished) return@LaunchedEffect
                if (imageRequests.size == 0) return@LaunchedEffect

                currentFrame = (currentFrame + 1) % (imageRequests.size)
            }
        }

//        // Change scale and offset when content size changes (e.g. rotation)
//        LaunchedEffect(contentWidth, contentHeight) {
//            if (contentWidth == 0 || contentHeight == 0) return@LaunchedEffect
//            if (imageWidth == 0f || imageHeight == 0f) return@LaunchedEffect
//
//            isLandscape = contentWidth > contentHeight
//            scale = max(contentHeight / imageHeight, contentWidth / imageWidth)
//            offset = Offset.Zero
//        }

        // Show the Map Animation Image
        BoxWithConstraints(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.TopCenter
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
            // Satellite image
            Column(
                Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                //println("Current frame: $currentFrame, ${imageRequests.size}")
                if (isInitialized) {
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
                                    scale =
                                        max(contentHeight / imageHeight, contentWidth / imageWidth)

                                    isFirstFrame = false
                                }
                            },
                        contentScale = ContentScale.None,
                    )
                }
            }

            var isLoadingMovie by remember { mutableStateOf(true) }
            if (is5DayMovieVisible) {
                Box(
                    Modifier.fillMaxSize(),
                ) {
                    VideoPlayer(
                        modifier = Modifier
                            .fillMaxSize(),
                        url = "https://www.ssec.wisc.edu/data/us_comp/us_comp_large.mp4",
                        onSetupComplete = {
                            isLoadingMovie = false
                        }
                    )

                    // Show 5 day movie
                    Row(
                        Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Button(
                            onClick = {
                                is5DayMovieVisible = false
                                isLoadingMovie = true
                            }
                        ) {
                            Text("Hide")
                        }
                        Spacer(modifier = Modifier.size(10.dp))

                        Text(
                            "Note: Please wait while entire movie loads into RAM due to ancient compression algorithm used by SSEC.",
                            color = Color.White
                        )

                    }

                    // Progress indicator
                    if (isLoadingMovie) {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            CircularProgressIndicator(
                                modifier = Modifier
                                    .height(50.dp),
                                color = Color.Red,
                            )
                        }
                    }
                }
            }
        }

        // Loading indicator
        AnimatedVisibility(
            !isLoadingFinished,
            enter = EnterTransition.None,
            exit = fadeOut(animationSpec = tween(1500))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .height(50.dp),
                    color = Color.Red,
                )
                Text(
                    "Loading... $finishedCount/$numFrames",
                    color = Color.White
                )
            }
        }

        // Controls
        AnimatedVisibility(!is5DayMovieVisible) {
            Column(
                Modifier.fillMaxHeight(),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Top,
            ) {
                AnimatedVisibility(isInternetConnectivityWarningVisible) {
                    Column(
                        Modifier
                            .fillMaxWidth()
                            .background(Color.Red.copy(alpha = 0.8f))
                        ,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            "No Internet Connection - Please check Wifi.",
                            color = Color.White,
                            modifier = Modifier.padding(10.dp)
                        )
                    }
                }

                // Show Controls
                Row(
                    Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.Top,
                    horizontalArrangement = Arrangement.Start
                ) {
                    Spacer(modifier = Modifier.size(8.dp))

                    // About
                    Button(
                        onClick = {
                            isShowAboutInfo = !isShowAboutInfo
                        },
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = MaterialTheme.colors.primary.copy(alpha = 0.5f),
                        ),
                        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.5f))

                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Help,
                            contentDescription = "Refresh",
                            tint = Color.White.copy(alpha = 0.5f)
                        )
                    }
                    Spacer(modifier = Modifier.size(10.dp))

                    // Toggle 4/12 hr map
                    Button(
                        onClick = {
                            is12HrMapVisible = !is12HrMapVisible
                            scope.launch {
                                scale = 1f
                                offset = Offset.Zero
                                isLoadingFinished = false
                                scope.launch {
                                    imageRequests.clear()
                                    isFirstFrame = true
                                    isInitialized = false
                                    finishedCount = 0
                                    currentFrame = -1
                                }
                            }
                        },
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = MaterialTheme.colors.primary.copy(alpha = 0.5f)
                        ),
                        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.5f))
                    ) {
                        Icon(
                            imageVector = if (is12HrMapVisible)
                                Icons.Filled.CalendarToday // 12hr
                            else
                                Icons.Filled.Today, // 4hr
                            contentDescription = "Choose Map",
                            tint = Color.White.copy(alpha = 0.5f)
                        )
                        Spacer(modifier = Modifier.size(5.dp))
                        Text(
                            if (is12HrMapVisible) "12hr" else "4hr",
                            color = Color.White.copy(alpha = 0.5f)
                        )
                    }
                    Spacer(modifier = Modifier.size(10.dp))

                    // Show 5 day movie
                    // Check if on Desktop (hide video for now)
                    if (!platform.name.contains("Java")) {
                        Button(
                            onClick = {
                                is5DayMovieVisible = true
                            },
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = MaterialTheme.colors.primary.copy(alpha = 0.5f)
                            ),
                            border = BorderStroke(1.dp, Color.White.copy(alpha = 0.5f))
                        ) {
                            Icon(
                                imageVector = Icons.Filled.CalendarViewWeek,
                                contentDescription = "Refresh",
                                tint = Color.White.copy(alpha = 0.5f)
                            )
                            Spacer(modifier = Modifier.size(5.dp))
                            Text(
                                "Show 5 day",
                                color = Color.White.copy(alpha = 0.5f)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.size(10.dp))

                    // Reload map, reset zoom & fit to screen
                    Button(
                        onClick = {
                            resetReloadMap()
                        },
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = MaterialTheme.colors.primary.copy(alpha = 0.5f),
                        ),
                        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.5f))
                    ) {
                        // material icon
                        Icon(
                            imageVector = Icons.Filled.Refresh,
                            contentDescription = "Refresh",
                            tint = Color.White.copy(alpha = 0.5f)
                        )
                    }
                    Spacer(modifier = Modifier.size(10.dp))

                    // Zoom (only on desktop bc pinch still doesn't work for some reason in 2024...)
                    // Check if on Desktop
                    if (platform.name.contains("Java")) {
                        Button(
                            onClick = {
                                scale *= 1.5f
                            },
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = MaterialTheme.colors.primary.copy(alpha = 0.5f),
                            ),
                            border = BorderStroke(1.dp, Color.White.copy(alpha = 0.5f))
                        ) {
                            Text(
                                "Zoom +",
                                color = Color.White.copy(alpha = 0.5f)
                            )
                        }
                        Spacer(modifier = Modifier.size(10.dp))

                        Button(
                            onClick = {
                                scale /= 1.5f
                            },
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = MaterialTheme.colors.primary.copy(alpha = 0.5f),
                            ),
                            border = BorderStroke(1.dp, Color.White.copy(alpha = 0.5f))
                        ) {
                            Text(
                                "Zoom -",
                                color = Color.White.copy(alpha = 0.5f)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.size(12.dp))
                }

                // Show debug info
                if (isDebugModeActive) {
                    Column(
                        Modifier
                            .fillMaxWidth()
                            .verticalScroll(rememberScrollState()),
                        horizontalAlignment = Alignment.Start,
                    ) {
                        Button(
                            onClick = {
                                showLog = !showLog
                            }
                        ) {
                            Text("Show Log")
                        }
                        Spacer(modifier = Modifier.size(10.dp))
                        if (showLog) {
                            Text(
                                loadingLog,
                                color = Color.White,
                                fontSize = 12.sp,
                                lineHeight = 12.sp
                            )
                        }
                    }
                }

                AnimatedVisibility(isShowAboutInfo) {
                    Text(
                        "Cloud Cover USA 2.0\n" +
                                "by Chris Athanas\n" +
                                "realityexpanderdev@gmail.com\n" +
                                "github.com/realityexpander\n",
                        color = Color.White,
                        modifier = Modifier.padding(10.dp)
                    )
                }

                // Drag & pinch notice
                if (!platform.name.contains("Java")) {
                    Box(
                        Modifier.fillMaxSize(),
                        contentAlignment = Alignment.BottomEnd,
                    ) {
                        Row(
                            Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.End
                        ) {
                            Text(
                                "Drag & Pinch controls map.",
                                color = Color.White.copy(alpha = 0.5f),
                                fontSize = 12.sp,
                                fontStyle = MaterialTheme.typography.body2.fontStyle,
                            )
                        }
                    }
                }

                // Show image loading progress
                if (finishedCount < numFrames - 1) {
                    LinearProgressIndicator(
                        progress = finishedCount / (numFrames - 1).toFloat(),
                        modifier = Modifier.fillMaxWidth()
                    )
                    Text(
                        "Loading current satellite frames...",
                        color = Color.White
                    )
                }
            }
        }
    }
}

