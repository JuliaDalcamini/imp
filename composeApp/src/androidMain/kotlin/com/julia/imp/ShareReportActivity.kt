package com.julia.imp

import android.content.Intent
import android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.core.content.FileProvider
import androidx.lifecycle.lifecycleScope
import com.julia.imp.common.ui.theme.ImpTheme
import com.julia.imp.report.ReportPage1
import ir.mahozad.multiplatform.comshot.captureToImage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream

class ShareReportActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            ImpTheme {
                LaunchedEffect(Unit) {
                    runOnUiThread {
                        val image = captureToImage(this@ShareReportActivity) { ReportPage1() }
                        shareReportImage(image.asAndroidBitmap())
                    }
                }
            }
        }
    }

    private fun shareReportImage(image: Bitmap) {
        lifecycleScope.launch {
            val imageUri = saveImageToShare("report.png", image)

            val shareIntent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                flags = flags or FLAG_GRANT_READ_URI_PERMISSION
                putExtra(Intent.EXTRA_STREAM, imageUri)
                setDataAndType(imageUri, "image/png")
            }

            startActivity(Intent.createChooser(shareIntent, null))
            finish()
        }
    }

    private suspend fun saveImageToShare(fileName: String, image: Bitmap): Uri? {
        return withContext(Dispatchers.IO) {
            val imagesFolder = File(cacheDir, "shared_images").also { it.mkdirs() }
            val file = File(imagesFolder, fileName)
            val stream = FileOutputStream(file)

            image.compress(Bitmap.CompressFormat.PNG, 100, stream)

            stream.flush()
            stream.close()

            FileProvider.getUriForFile(
                this@ShareReportActivity,
                "com.julia.imp.fileprovider",
                file
            )
        }
    }
}