package com.julia.imp.common.pdf

import androidx.compose.ui.graphics.ImageBitmap
import com.julia.imp.common.image.toUIImage
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.io.files.Path
import platform.CoreGraphics.CGRectMake
import platform.Foundation.writeToFile
import platform.UIKit.UIGraphicsPDFRenderer

private object IosPdfWriter : PdfWriter {

    @OptIn(ExperimentalForeignApi::class)
    override suspend fun createFromImages(images: List<ImageBitmap>, writeTo: Path) {
        val renderer = UIGraphicsPDFRenderer()

        val data = renderer.PDFDataWithActions { actions ->
            actions?.let {
                images.forEach { image ->
                    val uiImage = image.toUIImage()

                    val pageBounds = CGRectMake(
                        x = 0.0,
                        y = 0.0,
                        width = image.width.toDouble(),
                        height = image.height.toDouble()
                    )

                    actions.beginPageWithBounds(
                        bounds = pageBounds,
                        pageInfo = mapOf<Any?, Any>()
                    )

                    uiImage.drawInRect(pageBounds)
                }
            }
        }

        data.writeToFile(
            path = writeTo.toString(),
            atomically = true
        )
    }
}

actual fun getPdfWriter(): PdfWriter = IosPdfWriter