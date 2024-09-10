package com.julia.imp.report

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.layer.GraphicsLayer
import androidx.compose.ui.unit.dp
import com.julia.imp.common.ui.capture.recordOffscreen

@Composable
fun PrintableReportPage(
    captureLayer: GraphicsLayer,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier
            .requiredSize(1240.dp, 1754.dp)
            .recordOffscreen(captureLayer)
    ) {
        Box(Modifier.padding(64.dp)) {
            content()
        }
    }
}