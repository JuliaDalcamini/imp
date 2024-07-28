package com.julia.imp.common.pdf

import androidx.compose.ui.graphics.ImageBitmap

interface PdfWriter {

    /**
     * Creates a PDF file from a list of images.
     */
    suspend fun createFromImages(images: List<ImageBitmap>, writeTo: String)
}

expect fun getPdfWriter(): PdfWriter