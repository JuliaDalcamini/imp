package com.julia.imp.common.ui.details

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun Property(
    label: String,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Column(modifier) {
        Text(
            modifier = Modifier.padding(bottom = 4.dp),
            style = MaterialTheme.typography.labelMedium,
            text = label
        )

       content()
    }
}

@Composable
fun TextProperty(
    label: String,
    text: String,
    modifier: Modifier = Modifier
) {
    Property(
        modifier = modifier,
        label = label
    ) {
        Text(
            style = MaterialTheme.typography.bodyMedium,
            text = text
        )
    }
}