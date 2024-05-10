import android.graphics.Bitmap
import android.media.MediaPlayer
import android.os.Build
import android.widget.MediaController
import android.widget.VideoView
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.ColorInfo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.coroutines.coroutineContext

class AndroidPlatform : Platform {
    override val name: String = "Android ${Build.VERSION.SDK_INT}"
}

actual fun getPlatform(): Platform = AndroidPlatform()


@Composable
actual fun getScreenWidth() = LocalConfiguration.current
    .screenWidthDp
    .dp

@Composable
actual fun getScreenHeight() = LocalConfiguration.current
    .screenHeightDp
    .dp


@RequiresApi(Build.VERSION_CODES.P)
@Composable
actual fun VideoPlayer(
    modifier: Modifier,
    url: String,
    onSetupComplete: () -> Unit,
    onCloseVideoWindow: () -> Unit
) {

    AndroidView(
        modifier = modifier,
        factory = { context ->
            VideoView(context).apply {
                setVideoPath(url)
                val mediaController = MediaController(context)
                mediaController.setAnchorView(this)
                setMediaController(mediaController)
                mediaController.x = 0f
                mediaController.y = 0f

                setOnPreparedListener { mp ->
                    mp.setOnBufferingUpdateListener { mp, percent ->
                        println("Buffering $percent%...")
                    }
                    mp.setOnCompletionListener {
                        println("Video completed")
                    }
                    mp.setOnErrorListener { mp, what, extra ->
                        //println("Error: $what, $extra")
                        false
                    }
                    mp.setOnPreparedListener {
                        println("Video prepared")
                    }
                    mp.setOnVideoSizeChangedListener { mp, width, height ->
                        //println("Video size changed: $width x $height")
                    }
                    mp.setOnMediaTimeDiscontinuityListener { mp, timeStamp ->
                        //println("Media time discontinuity at $timeStamp, " +
                        //        "currentPosition: ${mp.currentPosition}, " +
                        //       "duration: ${mp.duration}")
                    }
                    mp.setOnTimedMetaDataAvailableListener { mp, data ->
                        //println("Timed metadata available: $data")
                    }
                    onSetupComplete()

                    mp.isLooping = true
                    mp.start()
                }
                setOnInfoListener { mp, what, extra ->
                    when (what) {
                        MediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START -> {
                            mp.start()
                            return@setOnInfoListener true
                        }

                        MediaPlayer.MEDIA_INFO_BUFFERING_START -> {
                            println("Buffering started")
                            return@setOnInfoListener true
                        }

                        MediaPlayer.MEDIA_INFO_BUFFERING_END -> {
                            println("Buffering ended")
                            return@setOnInfoListener true
                        }

                        else -> {
                            println("Unknown media info: $what, $extra")
                            return@setOnInfoListener false
                        }
                    }
                }
            }
        },
        update = {
            println("width: ${it.width}, height: ${it.height}")
        }
    )
}
