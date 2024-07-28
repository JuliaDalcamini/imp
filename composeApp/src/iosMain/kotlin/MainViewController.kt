import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.window.ComposeUIViewController
import com.julia.imp.App
import com.julia.imp.common.filesystem.appendPathComponent
import com.julia.imp.common.pdf.PdfViewerDelegate
import com.julia.imp.common.pdf.getPdfWriter
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.datetime.Clock.System
import platform.Foundation.NSCachesDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSSearchPathForDirectoriesInDomains
import platform.Foundation.NSString
import platform.Foundation.NSURL
import platform.Foundation.NSUserDomainMask
import platform.Foundation.stringByAppendingPathComponent
import platform.UIKit.UIDocumentInteractionController
import platform.UIKit.UIViewController

@Suppress("unused", "FunctionName")
fun MainViewController(): UIViewController {
    lateinit var controller: UIViewController

    controller = ComposeUIViewController {
        val coroutineScope = rememberCoroutineScope()
        App(onShowReportRequest = { coroutineScope.launch { showReport(it, controller) } })
    }

    return controller
}

private suspend fun showReport(images: List<ImageBitmap>, rootController: UIViewController) {
    val filePath: String

    withContext(Dispatchers.IO) {
        val reportDir = createReportDir()
        val fileName = "report-${System.now().toEpochMilliseconds()}.pdf"

        filePath = reportDir.appendPathComponent(fileName)
        getPdfWriter().createFromImages(images, filePath)
    }

    showPdf(filePath, rootController)
}

@OptIn(ExperimentalForeignApi::class)
private fun createReportDir(): String {
    val cacheDir = NSSearchPathForDirectoriesInDomains(
        directory = NSCachesDirectory,
        domainMask = NSUserDomainMask,
        expandTilde = true
    ).first() as? NSString

    if (cacheDir == null) throw IllegalStateException("Failed to get cache directory")

    val reportsDir = cacheDir.stringByAppendingPathComponent("reports")

    NSFileManager().createDirectoryAtPath(
        path = reportsDir,
        withIntermediateDirectories = true,
        attributes = null,
        error = null,
    )

    return reportsDir
}

private fun showPdf(path: String, rootController: UIViewController) {
    val fileUrl = NSURL.fileURLWithPath(path)
    val docController = UIDocumentInteractionController.interactionControllerWithURL(fileUrl)

    docController.delegate = PdfViewerDelegate(rootController)
    docController.presentPreviewAnimated(true)
}