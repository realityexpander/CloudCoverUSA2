package com.realityexpander.cloudcoverusa2

import App
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Preview
import com.seiko.imageloader.ImageLoader
import com.seiko.imageloader.LocalImageLoader
import com.seiko.imageloader.createDefaultAndroid
import commonConfig

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Fullscreen
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
//            window.decorView.windowInsetsController?.hide(WindowInsets.Type.systemBars())
//        } else {
//            @Suppress("DEPRECATION") // Older API support
//            window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_IMMERSIVE
//                    or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//                    or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//                    or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                    or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
//                    or View.SYSTEM_UI_FLAG_FULLSCREEN)
//        }

        // Hide action bar
        actionBar?.hide()

        setContent {
            CompositionLocalProvider(
                LocalImageLoader provides remember { generateImageLoader() },
            ) {
                App()
            }
        }
    }

    private fun generateImageLoader(): ImageLoader {
        return ImageLoader {
            takeFrom(ImageLoader.createDefaultAndroid(applicationContext))
            commonConfig()
        }
        //     return ImageLoader {
        //         commonConfig()
        //         options {
        //             androidContext(applicationContext)
        //         }
        //         components {
        //             setupDefaultComponents()
        //         }
        //         interceptor {
        //             // cache 25% memory bitmap
        //             bitmapMemoryCacheConfig {
        //                 maxSizePercent(context, 0.25)
        //             }
        //             // cache 50 image
        //             imageMemoryCacheConfig {
        //                 maxSize(50)
        //             }
        //             // cache 50 painter
        //             painterMemoryCacheConfig {
        //                 maxSize(50)
        //             }
        //             diskCacheConfig {
        //                 directory(cacheDir.resolve("image_cache").toOkioPath())
        //                 maxSizeBytes(512L * 1024 * 1024) // 512MB
        //             }
        //         }
        //     }
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    App()
}
