import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.window.ComposeUIViewController
import com.julia.imp.App
import com.julia.imp.common.pdf.getPdfWriter
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.datetime.Clock.System
import kotlinx.io.files.Path
import platform.Foundation.NSCachesDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSSearchPathForDirectoriesInDomains
import platform.Foundation.NSString
import platform.Foundation.NSURL
import platform.Foundation.NSUserDomainMask
import platform.Foundation.stringByAppendingPathComponent
import platform.UIKit.UIDocumentInteractionController
import platform.UIKit.UIDocumentInteractionControllerDelegateProtocol
import platform.UIKit.UIView
import platform.UIKit.UIViewController
import platform.darwin.NSObject

@Suppress("unused", "FunctionName")
fun MainViewController(): UIViewController {
    lateinit var controller: UIViewController

    controller = ComposeUIViewController {
        val coroutineScope = rememberCoroutineScope()
        App(onShowReportRequest = { coroutineScope.launch { showReport(it, controller) } })
    }

    return controller
}

@OptIn(ExperimentalForeignApi::class)
private suspend fun showReport(images: List<ImageBitmap>, rootController: UIViewController) {
    val reportPath: String

    withContext(Dispatchers.IO) {
        val cacheDir = NSSearchPathForDirectoriesInDomains(
            directory = NSCachesDirectory,
            domainMask = NSUserDomainMask,
            expandTilde = true
        ).first() as? NSString

        if (cacheDir == null) throw IllegalStateException("Failed to get cache directory")

        val relativeDirPath = "reports"
        val relativeFilePath = "$relativeDirPath/report-${System.now().toEpochMilliseconds()}.pdf"
        val reportsDir = cacheDir.stringByAppendingPathComponent(relativeDirPath)
        reportPath = cacheDir.stringByAppendingPathComponent(relativeFilePath)

        NSFileManager().createDirectoryAtPath(
            path = reportsDir,
            withIntermediateDirectories = true,
            attributes = null,
            error = null,
        )

        getPdfWriter().createFromImages(images, Path(reportPath))
    }

    val fileUrl = NSURL.fileURLWithPath(reportPath)
    val docController = UIDocumentInteractionController.interactionControllerWithURL(fileUrl)

    docController.delegate = object : NSObject(), UIDocumentInteractionControllerDelegateProtocol {

        override fun documentInteractionControllerViewForPreview(controller: UIDocumentInteractionController): UIView {
            return rootController.view
        }

        override fun documentInteractionControllerViewControllerForPreview(controller: UIDocumentInteractionController): UIViewController {
            return rootController
        }
    }

    docController.presentPreviewAnimated(true)
}