package com.julia.imp.common.report

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.layer.GraphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.julia.imp.common.ui.capture.recordOffscreen

@Composable
fun PrintableReportPage(
    captureLayer: GraphicsLayer,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    with(LocalDensity.current) {
        Box(
            modifier = modifier
                .requiredSize(2480.toDp(), 3508.toDp())
                .recordOffscreen(captureLayer)
        ) {
            Box(Modifier.padding(48.dp)) {
                content()
            }
        }
    }
}