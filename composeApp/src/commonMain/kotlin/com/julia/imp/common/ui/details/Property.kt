package com.julia.imp.common.ui.details

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp

@Composable
fun Property(
    label: String,
    modifier: Modifier = Modifier,
    labelStyle: TextStyle = MaterialTheme.typography.labelMedium,
    content: @Composable () -> Unit
) {
    Column(modifier) {
        Text(
            modifier = Modifier.padding(bottom = 4.dp),
            text = label,
            style = labelStyle
        )

       content()
    }
}

@Composable
fun TextProperty(
    label: String,
    text: String,
    modifier: Modifier = Modifier,
    labelStyle: TextStyle = MaterialTheme.typography.labelMedium,
    textStyle: TextStyle = MaterialTheme.typography.bodyMedium
) {
    Property(
        modifier = modifier,
        label = label,
        labelStyle = labelStyle
    ) {
        Text(
            text = text,
            style = textStyle
        )
    }
}