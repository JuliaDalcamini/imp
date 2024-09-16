package com.julia.imp.common.report

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun ReportField(
    label: String,
    value: String,
    modifier: Modifier = Modifier,
    horizontalAlignment: Alignment.Horizontal = Alignment.Start,
    verticalAlignment: Alignment.Vertical = Alignment.CenterVertically,
    textAlign: TextAlign = TextAlign.Unspecified
) {
    Column(
        modifier = modifier,
        horizontalAlignment = horizontalAlignment,
        verticalArrangement = Arrangement.spacedBy(
            space = 2.dp,
            alignment = verticalAlignment
        )
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelLarge,
            textAlign = textAlign
        )

        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            textAlign = textAlign
        )
    }
}