package com.julia.imp.common.file

import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import java.io.File

/**
 * Returns a shareable [Uri] for the [File].
 */
fun File.getShareableUri(context: Context, authority: String): Uri =
    FileProvider.getUriForFile(context, authority, this)