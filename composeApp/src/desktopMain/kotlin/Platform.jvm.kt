import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

class JVMPlatform: Platform {
    override val name: String = "Java ${System.getProperty("java.version")}"
}

actual fun getPlatform(): Platform = JVMPlatform()

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
