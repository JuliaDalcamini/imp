import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.julia.imp.App
import com.julia.imp.common.pdf.getPdfWriter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.awt.Desktop
import java.nio.file.Files
import kotlin.io.path.pathString

fun main() = application {
    val coroutineScope = rememberCoroutineScope()

    Window(
        onCloseRequest = ::exitApplication,
        title = "Imp",
    ) {
        App(
            onShowReportRequest = { coroutineScope.launch { shareReport(it) }}
        )
    }
}

private suspend fun shareReport(images: List<ImageBitmap>) {
    withContext(Dispatchers.IO) {
        val path = Files.createTempFile("report", ".pdf")

        getPdfWriter().createFromImages(
            images = images,
            writeTo = path.pathString
        )

        Desktop.getDesktop().open(path.toFile())
    }
}