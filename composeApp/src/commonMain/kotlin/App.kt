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
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.dp
import coil3.annotation.ExperimentalCoilApi
import coil3.compose.AsyncImage
import coil3.compose.LocalPlatformContext
import coil3.network.NetworkHeaders
import coil3.network.httpHeaders
import coil3.request.CachePolicy
import coil3.request.ImageRequest
import coil3.request.crossfade
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.jetbrains.compose.ui.tooling.preview.Preview
import kotlin.math.max
import kotlin.time.Duration.Companion.milliseconds

val platform = getPlatform()
const val isDebugModeActive = true

@OptIn(ExperimentalCoilApi::class)
@Composable
@Preview
fun App() {
	MaterialTheme {
		var showLog by remember { mutableStateOf(false) }
		var loadingLog by remember { mutableStateOf("Idle.") }
		var finishedCount by remember { mutableStateOf(0) }
		val localContext = LocalPlatformContext.current
		var imageWidth by remember { mutableStateOf(800f) }
		var imageHeight by remember { mutableStateOf(600f) }
		var isFirstFrame by remember { mutableStateOf(true) }
		val localScreenWidth = getScreenWidth()
		val localScreenHeight = getScreenHeight()
		var contentWidth by remember { mutableStateOf(0) }
		var contentHeight by remember { mutableStateOf(0) }
		var isLoadingFinished by remember { mutableStateOf(false) }
		var isBigMapVisible by remember { mutableStateOf(false) }
		var isShowAboutInfo by remember { mutableStateOf(false) }
		var isInitialized by remember { mutableStateOf(false) }
		var is5DayMovieVisible by remember { mutableStateOf(false) }
		val scope = rememberCoroutineScope()

//		val numFrames = 25 // 0-24
//        val numFrames = 9
		val numFrames by remember(isBigMapVisible) {
			mutableStateOf(if(isBigMapVisible) 25 else 8)
		}
		val rootUrl by remember(isBigMapVisible) {
			mutableStateOf(
				if(isBigMapVisible)
					"https://www.ssec.wisc.edu/data/us_comp/big/image"
				else
					"https://www.ssec.wisc.edu/data/us_comp/image"
			)
		}

		// check orientation
		var isLandscape by remember(localScreenWidth, localScreenHeight) {
			mutableStateOf(localScreenWidth > localScreenHeight)
		}

		val kMaxZoomInFactor = 5f
		var scale by remember { mutableStateOf(1f) }
		var offset by remember {
			mutableStateOf(Offset.Zero)
		}
		var currentFrame by remember { mutableStateOf(-1) }

		val imageRequests = remember {
			mutableListOf<ImageRequest>()
		}

		// Load 1 at a time for ios
		LaunchedEffect(finishedCount) {
//			delay(50)

			if (finishedCount >= numFrames) {
				isLoadingFinished = true
			} else {
				imageRequests.add(
					ImageRequest.Builder(localContext)
//						.data("https://www.ssec.wisc.edu/data/us_comp/big/image${finishedCount}.jpg")
						.data("$rootUrl$finishedCount.jpg")
						.crossfade(true) // prevents flicker
						.listener(
							onStart = { request ->
								// loadingLog =
								//	"$loadingLog\nImage $finishedCount started loading, finishedCount = ${request.httpHeaders["finishedCount"]}."
							},
							onSuccess = { request, response ->
								if (finishedCount < numFrames) {
									loadingLog =
										"$loadingLog\nImage $finishedCount loaded, key=${response.memoryCacheKey}."
									imageWidth = response.image.width.toFloat()
									imageHeight = response.image.height.toFloat()

									println("Image finishedCount: $finishedCount, numFrames: $numFrames, currentFrame: $currentFrame")

									finishedCount++
								}
							},
							onError = { request, throwable ->
								loadingLog =
									"$loadingLog\nImage ${request.diskCacheKey} failed to load, ${throwable.throwable.message}."
							},
							onCancel = { request ->
								val frameIdx = request.httpHeaders["finishedCount"]?.toInt()
								loadingLog =
									"$loadingLog\nImage cancelled, finishedCount = $frameIdx"
								println("Cancelled, request.httpHeaders: $frameIdx")

//								// Restart the request
//								frameIdx?.let{
//									if (frameIdx >= 0) {
//										imageRequests[frameIdx] = ImageRequest.Builder(localContext)
//											.data("https://www.ssec.wisc.edu/data/us_comp/big/image${frameIdx}.jpg")
//											.listener(request.listener)
//											.httpHeaders(
//												headers = NetworkHeaders.Builder().add("finishedCount", frameIdx.toString()).build()
//											)
////											.diskCachePolicy(CachePolicy.DISABLED) // Always load the latest upon launch
////											.memoryCachePolicy(CachePolicy.DISABLED) // Always load the latest upon launch
//											.networkCachePolicy(CachePolicy.DISABLED) // Always load the latest upon launch
//											.build()
//									}
//								}
							}
						)
						.diskCachePolicy(CachePolicy.DISABLED) // Always load the latest upon launch
//						.memoryCachePolicy(CachePolicy.DISABLED) // Always load the latest upon launch
//						.networkCachePolicy(CachePolicy.DISABLED) // Always load the latest upon launch
//						.networkCachePolicy(CachePolicy.ENABLED)
						.httpHeaders(
							headers = NetworkHeaders.Builder().add("finishedCount", finishedCount.toString()).build()
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
			if(imageRequests.size == 0) return@LaunchedEffect

			while (true) {
				if (currentFrame == 0)
					delay(500.milliseconds)
				else
					delay(100.milliseconds)

				if (!isLoadingFinished) return@LaunchedEffect
				if(imageRequests.size == 0) return@LaunchedEffect

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

			// Satellite image
			Column(
				Modifier.fillMaxWidth(),
				horizontalAlignment = Alignment.CenterHorizontally
			) {
				println("Current frame: $currentFrame, ${imageRequests.size}")
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
									scale = max(contentHeight / imageHeight, contentWidth / imageWidth)

									isFirstFrame = false
								}
							},
						contentScale = ContentScale.None,
					)
				}
			}

			// Controls
			Column(
				Modifier.fillMaxHeight(),
				horizontalAlignment = Alignment.Start
			) {
				Row(
					Modifier.fillMaxWidth(),
					verticalAlignment = Alignment.CenterVertically,
				) {
					Spacer(modifier = Modifier.size(10.dp))

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
						Text(
							"About",
							color = Color.White.copy(alpha = 0.5f)
						)
					}
					Spacer(modifier = Modifier.size(10.dp))

					// Show 5 day movie
					Button(
						onClick = {
							is5DayMovieVisible = true
						},
						colors = ButtonDefaults.buttonColors(
							backgroundColor = MaterialTheme.colors.primary.copy(alpha = 0.5f)
						),
						border = BorderStroke(1.dp, Color.White.copy(alpha = 0.5f))
					) {
						Text(
							"Show 5 Day",
							color = Color.White.copy(alpha = 0.5f)
						)
					}

					// Show 24 hr map
					Button(
						onClick = {
							isBigMapVisible = !isBigMapVisible
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
						Text(
							if(isBigMapVisible) "Hide 24 Hr" else "Show 24 Hr",
							color = Color.White.copy(alpha = 0.5f)
						)
					}
					Spacer(modifier = Modifier.size(10.dp))

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
					} else {
						Text(
							"Drag & Pinch\n" +
									"controls map.",
							color = Color.White.copy(alpha = 0.5f)
						)
					}
					Spacer(modifier = Modifier.size(10.dp))

					// Reset zoom & map
					Button(
						onClick = {
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

						},
						colors = ButtonDefaults.buttonColors(
							backgroundColor = MaterialTheme.colors.primary.copy(alpha = 0.5f),
						),
						border = BorderStroke(1.dp, Color.White.copy(alpha = 0.5f))
					) {
						Text(
							"Reset",
							color = Color.White.copy(alpha = 0.5f)
						)
					}
				}

				if (isDebugModeActive) {
					Button(
						onClick = {
							showLog = !showLog
						}
					) {
						Text("Show Log")
					}
				}
				if (showLog) {
					Text(
						loadingLog,
						color = Color.White
					)
				}

				if (isShowAboutInfo) {
					Text(
						"Cloud Cover USA 2.0\n" +
								"by Chris Athanas\n" +
								"realityexpanderdev@gmail.com\n" +
								"github.com/realityexpander\n",
						color = Color.White,
						modifier = Modifier.padding(10.dp)
					)
				}

				// Show image loading progress
//                if (finishedCount < 8) {
				if (finishedCount < numFrames - 1) {
					LinearProgressIndicator(
//                        progress = finishedCount / 7f,
						progress = finishedCount / (numFrames - 1).toFloat(),
						modifier = Modifier.fillMaxWidth()
					)
					Text(
						"Loading current satellite frames...",
						color = Color.White
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
//                        url = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4",
						onSetupComplete = {
							isLoadingMovie = false
						}
					)

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
							Text("Hide 7 Day Movie")
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
									.height(100.dp),
								color = Color.Red,
							)
						}
					}
				}
			}

		}
	}
}

