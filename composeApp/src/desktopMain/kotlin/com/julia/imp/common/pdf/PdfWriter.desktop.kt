package com.julia.imp.common.pdf

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.toAwtImage
import com.itextpdf.text.Document
import com.itextpdf.text.Image
import com.itextpdf.text.Rectangle
import java.io.File
import com.itextpdf.text.pdf.PdfWriter as ITextPdfWriter

private object DesktopPdfWriter : PdfWriter {

    override suspend fun createFromImages(images: List<ImageBitmap>, writeTo: String) {
        val document = Document()
        val file = File(writeTo)

        file.outputStream().use { outStream ->
            val writer = ITextPdfWriter.getInstance(document, outStream)

            writer.open()
            document.open()
            document.setMargins(0f, 0f, 0f, 0f)

            images.forEach { image ->
                document.pageSize = Rectangle(image.width.toFloat(), image.height.toFloat())
                document.newPage()
                document.add(Image.getInstance(image.toAwtImage(), null))
            }

            document.close()
            writer.close()
        }
    }
}

actual fun getPdfWriter(): PdfWriter = DesktopPdfWriter