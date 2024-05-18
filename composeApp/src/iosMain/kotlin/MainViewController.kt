import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.window.ComposeUIViewController
import com.seiko.imageloader.ImageLoader
import com.seiko.imageloader.LocalImageLoader
import com.seiko.imageloader.createDefaultIOS
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.darwin.Darwin

fun MainViewController() = ComposeUIViewController {
    CompositionLocalProvider(
        LocalImageLoader provides remember { generateImageLoader() },
    ) {
        App()
    }
}

private fun generateImageLoader(): ImageLoader {
    return ImageLoader {
        takeFrom(ImageLoader.createDefaultIOS())
        commonConfig()
    }
    // return ImageLoader {
    //     commonConfig()
    //     components {
    //         setupDefaultComponents()
    //     }
    //     interceptor {
    //         // cache 32MB bitmap
    //         bitmapMemoryCacheConfig {
    //             maxSize(32 * 1024 * 1024) // 32MB
    //         }
    //         // cache 50 image
    //         imageMemoryCacheConfig {
    //             maxSize(50)
    //         }
    //         // cache 50 painter
    //         painterMemoryCacheConfig {
    //             maxSize(50)
    //         }
    //         diskCacheConfig {
    //             directory(getCacheDir().toPath().resolve("image_cache"))
    //             maxSizeBytes(512L * 1024 * 1024) // 512MB
    //         }
    //     }
    // }
}

actual val httpEngine: HttpClientEngine
    get() = Darwin.create()
