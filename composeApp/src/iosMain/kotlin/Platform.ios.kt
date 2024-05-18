import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.interop.UIKitView
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.seiko.imageloader.ImageLoader
import com.seiko.imageloader.createDefaultIOS
import kotlinx.cinterop.CValue
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.useContents
import platform.AVFoundation.AVPlayer
import platform.AVFoundation.AVPlayerItem
import platform.AVFoundation.AVPlayerLayer
import platform.AVFoundation.AVPlayerLooper
import platform.AVFoundation.AVQueuePlayer
import platform.AVFoundation.addPeriodicTimeObserverForInterval
import platform.AVFoundation.play
import platform.AVKit.AVPlayerViewController
import platform.CoreGraphics.CGRect
import platform.CoreMedia.CMTimeMake
import platform.CoreMedia.CMTimeRangeMake
import platform.Foundation.NSURL
import platform.Foundation.NSUnitDuration.Companion.milliseconds
import platform.QuartzCore.CATransaction
import platform.QuartzCore.kCATransactionDisableActions
import platform.UIKit.UIDevice
import platform.UIKit.UIView

class IOSPlatform: Platform {
    override val name: String = UIDevice.currentDevice.systemName() + " " + UIDevice.currentDevice.systemVersion
}

actual fun getPlatform(): Platform = IOSPlatform()


@OptIn(ExperimentalComposeUiApi::class)
@Composable
actual fun getScreenWidth(): Dp = LocalWindowInfo.current
    .containerSize
    .width
    .dp


@OptIn(ExperimentalComposeUiApi::class)
@Composable
actual fun getScreenHeight(): Dp = LocalWindowInfo.current
    .containerSize
    .height
    .dp

@OptIn(ExperimentalForeignApi::class)
@Composable
actual fun VideoPlayer(
    modifier: Modifier,
    url: String,
    onSetupComplete: () -> Unit,
    onCloseVideoWindow: () -> Unit
) {
    val player = remember { AVPlayer(uRL = NSURL.URLWithString(url)!!) }
    val playerLayer = remember { AVPlayerLayer() }
    val avPlayerViewController = remember { AVPlayerViewController() }

    val player2 = remember { AVQueuePlayer() }
    val loopy = remember {
        AVPlayerLooper(
            player2,
            AVPlayerItem(NSURL.URLWithString(url)!!),
            CMTimeRangeMake(CMTimeMake(0, 1), duration = CMTimeMake(13,1)))
        }

    avPlayerViewController.player = player2
    avPlayerViewController.showsPlaybackControls = true

    var progressCount = 0

    playerLayer.player = player2
    // Use a UIKitView to integrate with your existing UIKit views
    UIKitView(
        factory = {
            // Create a UIView to hold the AVPlayerLayer
            val playerContainer = UIView()
            playerContainer.addSubview(avPlayerViewController.view)

            // Return the playerContainer as the root UIView
            playerContainer
        },
        onResize = { view: UIView, rect: CValue<CGRect> ->
            CATransaction.begin()
            CATransaction.setValue(true, kCATransactionDisableActions)
            view.layer.setFrame(rect)
            playerLayer.setFrame(rect)
            avPlayerViewController.view.layer.frame = rect
            CATransaction.commit()
        },
        update = { view ->
            player.play()
            avPlayerViewController.player!!.play()

            // setup delegate to detect when play starts
            player.addPeriodicTimeObserverForInterval(
                CMTimeMake(1, 1),
                queue = null,
                usingBlock = { time ->
                    time.useContents {
                        print("timeScape:  ${timescale.toDouble()}, ")
                        println("time: ${milliseconds.converter.valueFromBaseUnitValue(timescale.toDouble())}")

                        // Allow progress to be cleared after a few seconds
                        if(progressCount > 6) {
                            onSetupComplete()
                        }
                        progressCount++
                    }
                }
            )

        },
        modifier = modifier
    )
}
