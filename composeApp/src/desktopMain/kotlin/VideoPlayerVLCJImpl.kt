import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.awt.SwingPanel
import androidx.compose.ui.graphics.Color
import uk.co.caprica.vlcj.factory.discovery.NativeDiscovery
import uk.co.caprica.vlcj.player.base.MediaPlayer
import uk.co.caprica.vlcj.player.base.MediaPlayerEventAdapter
import uk.co.caprica.vlcj.player.component.CallbackMediaPlayerComponent
import uk.co.caprica.vlcj.player.component.EmbeddedMediaPlayerComponent
import java.awt.Component
import java.util.*

@Composable
fun VideoPlayerVLCJImpl(
	url: String,
	modifier: Modifier,
	onSetupComplete: () -> Unit = {},
	onHideProgress: () -> Unit = {},
	isVideoPlayerPaused: Boolean,
) {
	val mediaPlayerComponent = remember { initializeMediaPlayerComponent() }
	val mediaPlayer = remember { mediaPlayerComponent.mediaPlayer() }

	val factory = remember { { mediaPlayerComponent } }
	/* OR the following code and using SwingPanel(factory = { factory }, ...) */
	// val factory by rememberUpdatedState(mediaPlayerComponent)

	LaunchedEffect(url) {
		mediaPlayer.media().play/*OR .start*/(url)
		mediaPlayer.controls().repeat = true

		mediaPlayerComponent.mediaPlayer()
			.events()
			.addMediaPlayerEventListener(object : MediaPlayerEventAdapter() {
				override fun finished(mediaPlayer: MediaPlayer) {
					println("Video finished")
					//mediaPlayer.controls().play()
				}

				override fun error(mediaPlayer: MediaPlayer) {
					println("Error playing video")
				}

				override fun playing(mediaPlayer: MediaPlayer?) {
					super.playing(mediaPlayer)
					println("Video playing")
				}

				override fun paused(mediaPlayer: MediaPlayer?) {
					super.paused(mediaPlayer)
					println("Video paused")
				}

				override fun buffering(mediaPlayer: MediaPlayer?, newCache: Float) {
					super.buffering(mediaPlayer, newCache)
					println("Buffering $newCache%...")
				}

				override fun mediaPlayerReady(mediaPlayer: MediaPlayer?) {
					super.mediaPlayerReady(mediaPlayer)
					println("Media player ready")
					onHideProgress()
					onSetupComplete()
				}
			})
	}
	DisposableEffect(Unit) { onDispose(mediaPlayer::release) }

	LaunchedEffect(isVideoPlayerPaused) {
		if (isVideoPlayerPaused) {
			mediaPlayer.controls().pause()
		} else {
			mediaPlayer.controls().play()
		}
	}

	SwingPanel(
		factory = factory,
		background = Color.Transparent,
		modifier = modifier,
	)
}

private fun initializeMediaPlayerComponent(): Component {
	NativeDiscovery().discover()
	return if (isMacOS()) {
		CallbackMediaPlayerComponent()
	} else {
		EmbeddedMediaPlayerComponent()
	}
}


/**
 * Returns [MediaPlayer] from player components.
 * The method names are the same, but they don't share the same parent/interface.
 * That's why we need this method.
 */
private fun Component.mediaPlayer() = when (this) {
	is CallbackMediaPlayerComponent -> mediaPlayer()
	is EmbeddedMediaPlayerComponent -> mediaPlayer()
	else -> error("mediaPlayer() can only be called on vlcj player components")
}

private fun isMacOS(): Boolean {
	val os = System
		.getProperty("os.name", "generic")
		.lowercase(Locale.ENGLISH)
	return "mac" in os || "darwin" in os
}