package com.julia.imp.common.pdf

import android.graphics.Bitmap
import android.graphics.pdf.PdfDocument
import android.graphics.pdf.PdfDocument.PageInfo
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import kotlinx.io.files.Path
import java.io.File

private object AndroidPdfWriter : PdfWriter {

    override suspend fun createFromImages(images: List<ImageBitmap>, writeTo: Path) {
        val document = PdfDocument()

        images.forEachIndexed { index, image ->
            val pageInfo = PageInfo.Builder(image.width, image.height, index.inc()).create()
            val page = document.startPage(pageInfo)
            val bitmap = image.asAndroidBitmap().copy(Bitmap.Config.ARGB_8888, false)

            page.canvas.drawBitmap(bitmap, 0f, 0f, null)
            document.finishPage(page)
        }

        val file = File(writeTo.toString())

        document.writeTo(file.outputStream())
        document.close()
    }
}

actual fun getPdfWriter(): PdfWriter = AndroidPdfWriter