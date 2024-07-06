package com.julia.imp.report

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.rememberGraphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import com.julia.imp.common.ui.capture.recordOffscreen
import com.julia.imp.common.ui.theme.ImpTheme
import imp.composeapp.generated.resources.Res
import imp.composeapp.generated.resources.artifacts_title
import imp.composeapp.generated.resources.share_label
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportScreen(
    onBackClick: () -> Unit,
    onShareRequest: (List<ImageBitmap>) -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    val graphicsLayer = rememberGraphicsLayer()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(Res.string.artifacts_title)) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Outlined.ArrowBack, null)
                    }
                }
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                text = { Text(stringResource(Res.string.share_label)) },
                icon = { Icon(Icons.Default.Share, null) },
                onClick = {
                    coroutineScope.launch {
                        onShareRequest(listOf(graphicsLayer.toImageBitmap()))
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .consumeWindowInsets(paddingValues)
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            ReportPage1(
                modifier = Modifier
                    .fillMaxSize()
                    .align(Alignment.Center)
            )
        }

        PrintableReportPage(Modifier.recordOffscreen(graphicsLayer)) {
            ReportPage1(Modifier.fillMaxSize())
        }
    }
}

@Composable
fun PrintableReportPage(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    ImpTheme(darkTheme = false) {
        CompositionLocalProvider(LocalDensity provides Density(2f)) {
            Surface(
                modifier = modifier.requiredSize(1240.dp, 1754.dp),
                color = MaterialTheme.colorScheme.background,
            ) {
                content()
            }
        }
    }
}

@Composable
fun ReportPage1(modifier: Modifier = Modifier) {
    Box(modifier) {
        Text(
            modifier = Modifier.align(Alignment.Center),
            text = "Relatório",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onBackground
        )
    }
}