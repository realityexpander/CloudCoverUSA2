import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Help
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.CalendarViewWeek
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Today
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.ImageLoader
import coil3.SingletonImageLoader
import coil3.annotation.ExperimentalCoilApi
import coil3.compose.AsyncImagePainter
import coil3.compose.LocalPlatformContext
import coil3.compose.rememberAsyncImagePainter
import coil3.network.NetworkHeaders
import coil3.network.httpHeaders
import coil3.request.ImageRequest
import coil3.request.ImageResult
import coil3.request.crossfade
import com.seiko.imageloader.ImageLoaderConfigBuilder
import com.seiko.imageloader.intercept.Interceptor
import com.seiko.imageloader.model.ImageAction
import com.seiko.imageloader.model.ImageRequestBuilder
import com.seiko.imageloader.model.NullRequestData
import com.seiko.imageloader.rememberImageSuccessPainter
import com.seiko.imageloader.toPainter
import com.seiko.imageloader.ui.AutoSizeBox
import com.seiko.imageloader.util.LogPriority
import com.seiko.imageloader.util.Logger
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.serialization.kotlinx.json.json
import io.ktor.utils.io.InternalAPI
import io.ktor.utils.io.core.readBytes
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.jetbrains.compose.ui.tooling.preview.Preview
import kotlin.math.max
import kotlin.time.Duration.Companion.milliseconds

val platform = getPlatform()
const val isDebugModeActive = false

@OptIn(ExperimentalCoilApi::class, InternalAPI::class, ExperimentalAnimationApi::class)
@Composable
@Preview
fun App() {
    val imageRequests = mutableListOf<ImageRequest>()

    val loadedImageCount = MutableStateFlow(0)
    val imageResults = MutableStateFlow(mutableListOf<ImageResult?>())

    var currentAnimFrame by remember { mutableStateOf(0) }
    var finishedCount by remember { mutableStateOf(0) }

    var isInternetConnectivityWarningVisible by remember { mutableStateOf(false) }
    var isInitialized by remember { mutableStateOf(false) }

    val isAndroidPlatform = platform.name.contains("Android")
    val isDesktopPlatform = platform.name.contains("Java")
//    var isAndroidInitialized by remember { mutableStateOf(!isAndroidPlatform && !isDesktopPlatform) } // compose-imageloader // LEAVE FOR REFERENCE
    var isAndroidInitialized by remember { mutableStateOf(false) }

    val byteArrayImages = MutableStateFlow(mutableListOf<ByteArray?>())
    var ir by remember { mutableStateOf<com.seiko.imageloader.model.ImageRequest?>(null) }
    val imageRequestsSeiko =
        MutableStateFlow(mutableListOf<com.seiko.imageloader.model.ImageRequest?>())


    MaterialTheme {
        val localContext = LocalPlatformContext.current
        val scope = rememberCoroutineScope { Dispatchers.Main }

        var is12HrMapVisible by remember { mutableStateOf(false) }
        var isShowAboutInfo by remember { mutableStateOf(false) }
        var is5DayMovieVisible by remember { mutableStateOf(false) }

        // Log
        var isDebugLogVisible by remember { mutableStateOf(false) }
        var debugLog by remember { mutableStateOf("Idle.") }

        // Animation specs
        val numFrames by remember(is12HrMapVisible) {
            mutableStateOf(if (is12HrMapVisible) 25 else 8)
        }
        val rootUrl by remember(is12HrMapVisible) {
            mutableStateOf(
                if (is12HrMapVisible)
//					"https://www.ssec.wisc.edu/data/us_comp/big/image"
                    "https://wsrv.nl/?url=https://www.ssec.wisc.edu/data/us_comp/big/image" // using proxy for js target
                else
//					"https://www.ssec.wisc.edu/data/us_comp/image"
                    "https://wsrv.nl/?url=https://www.ssec.wisc.edu/data/us_comp/image" // using proxy for js target
            )
        }
        var isFirstFrame by remember { mutableStateOf(true) }
        var imageWidth by remember { mutableStateOf(800f) }
        var imageHeight by remember { mutableStateOf(600f) }
        val localScreenWidth = getScreenWidth()
        val localScreenHeight = getScreenHeight()
        var contentWidth by remember { mutableStateOf(0) }
        var contentHeight by remember { mutableStateOf(0) }

        // check orientation
        var isLandscape by remember(localScreenWidth, localScreenHeight) {
            mutableStateOf(localScreenWidth > localScreenHeight)
        }

        // Zoom & Pan
        val kMaxZoomInFactor = 5f
        var scale by remember { mutableStateOf(1f) }
        var offset by remember { mutableStateOf(Offset.Zero) }

        // Load Images Parallel
        val localPlatformContext = LocalPlatformContext.current
        val imageLoader = ImageLoader(localPlatformContext)

        val ioScope = rememberCoroutineScope()

        // Reset the map
        var shouldReset by remember { mutableStateOf(false) }
        fun resetReloadMap() {
            scale = 1f
            offset = Offset.Zero
            loadedImageCount.update { 0 }
            scope.launch {
                SingletonImageLoader.get(localContext).diskCache?.clear()
                SingletonImageLoader.get(localContext).memoryCache?.clear()
                isFirstFrame = true
                isInitialized = false
                currentAnimFrame = 0
                finishedCount = 0
                isAndroidInitialized = false

                shouldReset = !shouldReset // triggers reset by changing the value
            }
        }

        fun addToDebugLog(msg: String) {
            debugLog = "$msg\n$debugLog"
            println(msg)
        }

        // Load the images in parallel
        LaunchedEffect(shouldReset, numFrames) {
            imageRequests.clear()
            finishedCount = 0

            // Create requests to pre-load images.
                val random = (0..1_000_000).random() // allows for cache busting
            var frame = 0
            while(frame < numFrames) {
                val url = "$rootUrl$frame.jpg?$random"
                val imageRequest = ImageRequest.Builder(localPlatformContext)
                    .data(url)
                    .listener(
                        onStart = { request ->
                            // addToDebugLog("Image $finishedCount started loading, frame = ${request.httpHeaders["user-agent"]}.")
                        },
                        onSuccess = { request, response ->
                            if (finishedCount < numFrames) {
                                addToDebugLog("Image $finishedCount onSuccess, key=${response.memoryCacheKey}.")
                                imageWidth = response.image.width.toFloat()
                                imageHeight = response.image.height.toFloat()

                                finishedCount++
                                isInternetConnectivityWarningVisible = false
                            }
                        },
                        onError = { request, throwable ->
                            addToDebugLog("onError, Failed to load, diskCacheKey = ${request.diskCacheKey}, " +
                                    "frame = ${request.httpHeaders["user-agent"]}, " +
                                    "${throwable.throwable.message}.")

                            // Restart from 0
                            frame = numFrames // stop loading
                            scope.launch {
                                isInternetConnectivityWarningVisible = true
                                delay(5000.milliseconds)

                                resetReloadMap()
                            }
                        },
                        onCancel = { request ->
                            val frameIdx = request.httpHeaders["user-agent"]?.toInt()
                            addToDebugLog("onCancel, Image frameIdx = $frameIdx, diskCacheKey= ${request.diskCacheKey}")
                        }
                    )
                    .crossfade(isAndroidPlatform)
                    .diskCacheKey(url)
                    .httpHeaders(NetworkHeaders.Builder().set("user-agent", frame.toString()).build()) // overloading the `user-agent` header to pass frame info (hacky)
                    .build()

                imageRequests += imageRequest
                frame++
            }

            // LEAVE FOR REFERENCE (compose-imageloader)
//            byteArrayImages.update { // (compose-imageloader using ByteArray)
//                mutableListOf<ByteArray?>().apply {
//                    repeat(numFrames) { add(null) }
//                }
//            }
//            imageRequestsSeiko.update {  // (compose-imageloader using Request)
//                mutableListOf<com.seiko.imageloader.model.ImageRequest?>().apply {
//                    repeat(numFrames) { add(null) }
//                }
//            }
            imageResults.update { mutableListOf<ImageResult?>().apply { repeat(numFrames) { add(null) } } }
            imageRequests.forEachIndexed { index, imageRequest ->
                imageResults.update { imageResults ->
                    ioScope.launch {
                        imageResults[index] = imageLoader.execute(imageRequest) // pre-loads cache
                        loadedImageCount.update { it + 1 }

                        if (loadedImageCount.value >= numFrames)
                            isInitialized = true
                    }

                    imageResults
                }

                // LEAVE FOR REFERENCE (compose-imageloader)
//                // Manually load the images (compose-imageloader using raw-byte-data (ByteArray))
//                byteArrayImages.update { byteArrayImages ->
//                    ioScope.launch {
//                        val random = (0..1_000_000).random() // allows for cache busting
//                        val result =
//                            client.get("$rootUrl$index.jpg?$random")
//                                .content
//                                .readRemaining()
//                                .readBytes()
//
//                        byteArrayImages[index] = result
//                    }
//
//                    byteArrayImages
//                }

//                imageRequestsSeiko.update { imageRequests2 ->  // (compose-imageloader using Request)
//                    val random = (0..1_000_000).random() // allows for cache busting
//                    imageRequests2[index] =
//                        com.seiko.imageloader.model.ImageRequest("$rootUrl$index.jpg?$random")
//
//                    imageRequests2
//                }
            }
        }


        // Run the animation frames
        LaunchedEffect(isInitialized) {
            if (!isInitialized) return@LaunchedEffect

            var totalFrames = 0
            while (true) {
                if (currentAnimFrame == 0)
                    delay(500.milliseconds)
                else
                    delay(150.milliseconds)

                if (!isInitialized) return@LaunchedEffect
                currentAnimFrame = (currentAnimFrame + 1) % numFrames

                totalFrames++
                if (totalFrames > imageResults.value.size * 2) { // 2 passes (hack to hide the loading on Android)
                    isAndroidInitialized = true
                }
            }
        }

        // LEAVE FOR FUTURE USE
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
            modifier = Modifier
                .background(Color.Black)
                .fillMaxSize(),
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

            // Show Satellite Animation (flip thru frames)
            Column(
                Modifier
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (isInitialized) {
                    imageResults.value[currentAnimFrame]?.image?.let { imageResult ->  // Use with Image
//                    byteArrayImages.value[currentAnimFrame]?.let { imageByteArray -> // Use with ImageItem (compose-imageloader)

                        // https://github.com/coil-kt/coil/issues/2246
                        Image(
                            // JetBrains Standard Composable Image
//                      ImageItem( // (compose-imageloader)

                            // (coil3 using Request)
                            rememberAsyncImagePainter(imageRequests[currentAnimFrame]), // Android & All Platforms, use Image, works smoothly BEST FOR ALL PLATFORMS
//                        rememberAsyncImagePainter(imageResults.value[currentAnimFrame]!!.request), // Android & All Platforms, works smoothly, except JS uses low-res image

                            // (compose-imageloader)
//                            bitmap = imageResult.toBitmap().asComposeImageBitmap(), // Non-Android only, use Image, WORKS smoothly, BEST for JS
//                            org.jetbrains.skia.Image.makeFromEncoded(imageByteArray).toComposeImageBitmap(), // Non-android only, works smoothly

//                            rememberAsyncImagePainter(imageByteArray), // works but flashy
//                            rememberAsyncImagePainter(byteArrayImages.value[currentAnimFrame]), // works but flashy
//                            rememberAsyncImagePainter(imageByteArray), // works but flashy
//                            imageByteArray, // works with ImageItem, flashy
//                            byteArrayImages.value[currentAnimFrame]!!, // works w/ ImageItem, flashy
//                            remember(currentAnimFrame) { byteArrayImages.value[currentAnimFrame]!! }, // works w/ ImageItem, flashy

                            contentDescription = "image",
                            modifier = Modifier
                                .fillMaxSize()
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

                                    // Scale to fit in window/screen
                                    if (isFirstFrame && contentWidth > 0 && imageWidth > 0) {
                                        // Set correct scale for a given image and screen size
                                        scale =
                                            max(
                                                contentHeight / imageHeight,
                                                contentWidth / imageWidth
                                            )

                                        isFirstFrame = false
                                    }
                                },
                            contentScale = ContentScale.None,
                        )
                    }
                }
            }
        }

        // Loading indicator (to hide image loading flicker)
        AnimatedVisibility(
            loadedImageCount.collectAsState().value < numFrames
                    || !isAndroidInitialized // Hack to hide loading on Android using rememberAsyncImagePainter
            ,
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
                    "Loading... ${loadedImageCount.collectAsState().value}/$numFrames",
                    color = Color.White
                )
            }
        }

        var isLoadingMovie by remember { mutableStateOf(true) }
        if (is5DayMovieVisible) {
            Box(
                Modifier
                    .fillMaxSize()
                    .background(Color.Black),
            ) {

                VideoPlayer(
                    modifier = Modifier
                        .fillMaxSize()
                        .offset(x = 0.dp, y = 50.dp), // offset to avoid overlap with hide button
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
                        },
                    ) {
                        Text(
                            "Hide",
                            color=Color.White
                        )
                    }

                    // Progress indicator
                    AnimatedVisibility(
                        isLoadingMovie,
                        enter = EnterTransition.None,
                        exit = fadeOut(animationSpec = tween(5500))
                    ) {
                        Row(
                            Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Spacer(modifier = Modifier.width(10.dp))
                            CircularProgressIndicator(
                                modifier = Modifier.width(20.dp),
                                color = Color.Red,
                            )
                            Spacer(modifier = Modifier.size(10.dp))

                            Text(
                                "Note: Please wait while entire movie loads into RAM due to ancient compression algorithm used by SSEC.",
                                color = Color.White
                            )
                        }
                    }

                }
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
                            .background(Color.Red.copy(alpha = 0.8f)),
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
                            resetReloadMap()
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
                    // Hide video on desktop, for now.
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
                                isDebugLogVisible = !isDebugLogVisible
                            }
                        ) {
                            Text("Show Log")
                        }
                        Spacer(modifier = Modifier.size(10.dp))
                        if (isDebugLogVisible) {
                            Text(
                                debugLog,
                                color = Color.White,
                                fontSize = 12.sp,
                                lineHeight = 12.sp
                            )
                        }
                    }
                }

                // About info
                AnimatedVisibility(isShowAboutInfo) {
                    Text(
                        "Cloud Cover USA 2.1\n" +
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
            }
        }
    }
}

@Composable
fun ImageItem(
    data: Any,
    modifier: Modifier = Modifier.aspectRatio(1f),
    contentScale: ContentScale = ContentScale.Crop,
    contentDescription: String = "image",
    block: (ImageRequestBuilder.() -> Unit)? = null,
) {
    Box(modifier, Alignment.Center) {
        val dataState by rememberUpdatedState(data)
        val blockState by rememberUpdatedState(block)
        val request by remember {
            derivedStateOf {
                com.seiko.imageloader.model.ImageRequest {
                    data(dataState)
                    addInterceptor(NullDataInterceptor)
                    // components {
                    //     add(customKtorUrlFetcher)
                    // }
                    options {
                        maxImageSize = 512
                    }
                    blockState?.invoke(this)
                }
            }
        }

        AutoSizeBox(
            request,
            Modifier.matchParentSize(),
        ) { action ->
            when (action) {
                is ImageAction.Loading -> {
                    CircularProgressIndicator()
                }

                is ImageAction.Success -> {
                    Image(
                        rememberImageSuccessPainter(action),
                        contentDescription = contentDescription,
                        contentScale = contentScale,
                        modifier = Modifier.matchParentSize(),
                    )
                }

                is ImageAction.Failure -> {
                    Text(action.error.message ?: "Error")
                }
            }
        }
    }
}

// For Compose-imageloader
fun ImageLoaderConfigBuilder.commonConfig() {
    logger = object : Logger {
        override fun log(
            priority: LogPriority,
            tag: String,
            data: Any?,
            throwable: Throwable?,
            message: String,
        ) {
//            DebugLogger.log(
//                severity = when (priority) {
//                    LogPriority.VERBOSE -> Severity.Verbose
//                    LogPriority.DEBUG -> Severity.Debug
//                    LogPriority.INFO -> Severity.Info
//                    LogPriority.WARN -> Severity.Warn
//                    LogPriority.ERROR -> Severity.Error
//                    LogPriority.ASSERT -> Severity.Assert
//                },
//                tag = tag,
//                throwable = throwable,
//                message = buildString {
//                    if (data != null) {
//                        append("[image data] ")
//                        append(data.toString().take(100))
//                        append('\n')
//                    }
//                    append("[message] ")
//                    append(message)
//                },
//            )
            println(
                "[$tag] $priority: $message, ${
                    data.toString().take(100)
                }, ${throwable?.message}"
            )
        }

        override fun isLoggable(priority: LogPriority) = priority >= LogPriority.DEBUG
    }
    components {
        // add(MokoResourceFetcher.Factory())
        //add(ComposeResourceFetcher.Factory())
    }
    interceptor {
        //addInterceptor(NinePatchInterceptor())
    }
}

/**
 * return empty painter if request is null or empty - For Compose-imageloader
 */
object NullDataInterceptor : Interceptor {

    override suspend fun intercept(chain: Interceptor.Chain): com.seiko.imageloader.model.ImageResult {
        val data = chain.request.data
        if (data === NullRequestData || data is String && data.isEmpty()) {
            return com.seiko.imageloader.model.ImageResult.OfPainter(
                painter = EmptyPainter,
            )
        }
        return chain.proceed(chain.request)
    }

    private object EmptyPainter : Painter() {
        override val intrinsicSize: Size get() = Size.Unspecified
        override fun DrawScope.onDraw() {}
    }
}


