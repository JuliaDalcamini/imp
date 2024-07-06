package com.julia.imp.common.pdf

import androidx.compose.ui.graphics.ImageBitmap
import kotlinx.io.files.Path

private object IosPdfWriter : PdfWriter {

    override suspend fun createFromImages(images: List<ImageBitmap>, writeTo: Path) {
        TODO("Not yet implemented")
    }
}

actual fun getPdfWriter(): PdfWriter = IosPdfWriter