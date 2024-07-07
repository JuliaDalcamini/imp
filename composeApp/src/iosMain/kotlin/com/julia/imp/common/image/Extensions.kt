package com.julia.imp.common.image

import androidx.compose.ui.graphics.ImageBitmap
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.refTo
import platform.CoreGraphics.CGBitmapContextCreate
import platform.CoreGraphics.CGBitmapContextCreateImage
import platform.CoreGraphics.CGColorSpaceCreateDeviceRGB
import platform.CoreGraphics.CGImageAlphaInfo
import platform.UIKit.UIImage

@OptIn(ExperimentalForeignApi::class)
fun ImageBitmap.toUIImage(): UIImage {
    val width = this.width
    val height = this.height
    val buffer = IntArray(width * height)
    val colorSpace = CGColorSpaceCreateDeviceRGB()

    this.readPixels(buffer)

    val context = CGBitmapContextCreate(
        data = buffer.refTo(0),
        width = width.toULong(),
        height = height.toULong(),
        bitsPerComponent = 8u,
        bytesPerRow = (4 * width).toULong(),
        space = colorSpace,
        bitmapInfo = CGImageAlphaInfo.kCGImageAlphaPremultipliedLast.value
    )

    val uiImage = CGBitmapContextCreateImage(context)?.let {
        UIImage.imageWithCGImage(it)
    }

    return uiImage ?: throw IllegalStateException("Failed to create UIImage")
}