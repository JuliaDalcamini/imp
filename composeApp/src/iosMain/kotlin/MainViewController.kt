import androidx.compose.ui.window.ComposeUIViewController
import com.julia.imp.App

@Suppress("unused", "FunctionName")
fun MainViewController() = ComposeUIViewController { App(onShareReportRequest = {}) }