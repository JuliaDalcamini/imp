package com.julia.imp.common.report

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.rememberGraphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import com.julia.imp.common.ui.theme.ImpTheme
import kotlinx.coroutines.delay

@Composable
fun ReportGenerator(
    vararg pages: @Composable () -> Unit,
    onReportReady: (List<ImageBitmap>) -> Unit
) {
    val captureLayers = pages.map { rememberGraphicsLayer() }

    ImpTheme(darkTheme = false) {
        CompositionLocalProvider(LocalDensity provides Density(3f)) {
            pages.forEachIndexed { index, page ->
                PrintableReportPage(captureLayers[index]) { page() }
            }
        }
    }

    LaunchedEffect(Unit) {
        delay(1000)
        onReportReady(captureLayers.map { it.toImageBitmap() })
    }
}