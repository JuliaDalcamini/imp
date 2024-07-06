package com.julia.imp.common.pdf

import androidx.compose.ui.graphics.ImageBitmap
import kotlinx.io.files.Path

interface PdfWriter {

    /**
     * Creates a PDF file from a list of images.
     */
    suspend fun createFromImages(images: List<ImageBitmap>, writeTo: Path)
}

expect fun getPdfWriter(): PdfWriter