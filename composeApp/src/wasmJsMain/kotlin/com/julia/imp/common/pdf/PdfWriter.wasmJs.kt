package com.julia.imp.common.pdf

import androidx.compose.ui.graphics.ImageBitmap

private object WasmJsPdfWriter : PdfWriter {

    override suspend fun createFromImages(images: List<ImageBitmap>, writeTo: String) {
        // TODO: Implement
    }
}

actual fun getPdfWriter(): PdfWriter = WasmJsPdfWriter