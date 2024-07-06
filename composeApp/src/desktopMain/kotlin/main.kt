import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.julia.imp.App

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "Imp",
    ) {
        App(onShareReportRequest = {})
    }
}