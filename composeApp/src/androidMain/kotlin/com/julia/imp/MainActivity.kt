package com.julia.imp

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.ui.graphics.ImageBitmap
import androidx.lifecycle.lifecycleScope
import com.julia.imp.common.file.getShareableUri
import com.julia.imp.common.pdf.getPdfWriter
import imp.composeapp.generated.resources.Res
import imp.composeapp.generated.resources.share_report_title
import kotlinx.coroutines.launch
import kotlinx.io.files.Path
import org.jetbrains.compose.resources.stringResource
import java.io.File

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val shareReportTitle = stringResource(Res.string.share_report_title)

            App(
                onShareReportRequest = { images ->
                    shareReport(
                        title = shareReportTitle,
                        images = images
                    )
                }
            )
        }
    }

    private fun shareReport(title: String, images: List<ImageBitmap>) {
        lifecycleScope.launch {
            val imagesFolder = File(cacheDir, "reports").also { it.mkdirs() }
            val file = File(imagesFolder, "report-${System.currentTimeMillis()}.pdf")

            getPdfWriter().createFromImages(
                images = images,
                writeTo = Path(file.path)
            )

            shareFile(
                title = title,
                uri = file.getShareableUri(this@MainActivity, "com.julia.imp.fileprovider"),
                type = "application/pdf"
            )
        }
    }

    private fun shareFile(title: String, uri: Uri, type: String) {
        val intent = Intent(Intent.ACTION_SEND).apply {
            putExtra(Intent.EXTRA_STREAM, uri)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            setDataAndType(uri, type)
        }

        startActivity(Intent.createChooser(intent, title), null)
    }
}