package com.julia.imp.common.ui.title

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow

@Composable
fun CompoundTitle(
    title: String,
    subtitle: String,
    modifier: Modifier = Modifier
) {
    Column(modifier) {
        Title(title)

        Text(
            text = subtitle,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.bodySmall
        )
    }
}