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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            App(onShowReportRequest = { saveAndOpenReport(it) })
        }
    }

    private fun saveAndOpenReport(images: List<ImageBitmap>) {
        lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                val reportsDir = File(cacheDir, "reports").also { it.mkdirs() }
                val file = File(reportsDir, "report-${System.currentTimeMillis()}.pdf")

                getPdfWriter().createFromImages(
                    images = images,
                    writeTo = file.path
                )

                openPdf(file.getShareableUri(this@MainActivity, "com.julia.imp.fileprovider"))
            }
        }
    }

    private fun openPdf(uri: Uri) {
        val intent = Intent(Intent.ACTION_VIEW).apply {
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            setDataAndType(uri, "application/pdf")
        }

        startActivity(Intent.createChooser(intent, null), null)
    }
}