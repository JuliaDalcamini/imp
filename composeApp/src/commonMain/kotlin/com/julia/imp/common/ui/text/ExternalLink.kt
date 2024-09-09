package com.julia.imp.common.ui.text

import androidx.compose.foundation.clickable
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import com.julia.imp.common.text.toLink

@Composable
fun ExternalLink(
    url: String,
    modifier: Modifier = Modifier
) {
    val uriHandler = LocalUriHandler.current

    Text(
        modifier = modifier.clickable { uriHandler.openUri(url) },
        style = MaterialTheme.typography.bodyMedium,
        text = url.toLink()
    )
}