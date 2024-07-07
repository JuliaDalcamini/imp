package com.julia.imp.report

import androidx.compose.animation.core.snap
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.layer.GraphicsLayer
import androidx.compose.ui.graphics.rememberGraphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import com.julia.imp.common.ui.capture.recordOffscreen
import com.julia.imp.common.ui.theme.ImpTheme
import io.github.koalaplot.core.pie.PieChart
import io.github.koalaplot.core.util.ExperimentalKoalaPlotApi
import kotlinx.coroutines.delay
import kotlin.random.Random

@Composable
fun ReportGenerator(
    vararg pages: @Composable () -> Unit,
    onReportReady: (List<ImageBitmap>) -> Unit
) {
    val captureLayers = pages.map { rememberGraphicsLayer() }

    ImpTheme(darkTheme = false) {
        CompositionLocalProvider(LocalDensity provides Density(2f)) {
            pages.forEachIndexed { index, page ->
                PrintableReportPage(captureLayers[index]) { page() }
            }
        }
    }

    LaunchedEffect(Unit) {
        delay(200)
        onReportReady(captureLayers.map { it.toImageBitmap() })
    }
}

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

@Composable
fun ProjectReportGenerator(onReportReady: (List<ImageBitmap>) -> Unit) {
    ReportGenerator(
        { ProjectReportPage1(Modifier.fillMaxSize()) },
        { ProjectReportPage2(Modifier.fillMaxSize()) },
        onReportReady = onReportReady
    )
}

@Composable
fun ProjectReportPage1(modifier: Modifier = Modifier) {
    Column(modifier) {
        Text(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            text = "Relatório do Projeto",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onBackground
        )

        Table(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp),
            headers = listOf("Nome", "Tipo", "Valor"),
            rows = listOf(
                listOf("Item de teste número 1", "Artefato de inspeção de software", "R$ 1.000,00"),
                listOf("Item número 2", "Artefato que faz alguma coisa", "R$ 1.200,00"),
                listOf("Esse item é o terceiro", "Artefato que faz outra coisa", "R$ 1.500,00"),
                listOf("Acho que esse é o quarto", "Artefato que faz outra coisa", "R$ 1.500,00"),
                listOf("E esse é o quinto", "Artefato que faz outra coisa", "R$ 1.500,00"),
                listOf("Mas esse outro é o sexto", "Artefato que faz mais alguma outra coisa", "R$ 1.700,00"),
                listOf("Já esse aqui é o sétimo", "Artefato que faz uma coisa diferente", "R$ 1.860,00"),
                listOf("Esse eu acredito que seja o oitavo", "Será que esse artefato faz algo?", "R$ 102.563,00"),
                listOf("Artefato 9", "Esse aqui acho que é igual aos outros", "R$ 1.540,21"),
            ),
            columnWeights = listOf(1f, 1f, .5f)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            ExampleChart()
            ExampleChart()
            ExampleChart()
        }
    }
}

@Composable
fun ProjectReportPage2(modifier: Modifier = Modifier) {
    Column(modifier) {
        Text(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            text = "Relatório do Projeto",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onBackground
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            ExampleChart()
            ExampleChart()
            ExampleChart()
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            ExampleChart()
            ExampleChart()
            ExampleChart()
        }

        Table(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp),
            headers = listOf("Nome", "Tipo", "Valor"),
            rows = listOf(
                listOf("Item de teste número 1", "Artefato de inspeção de software", "R$ 1.000,00"),
                listOf("Item número 2", "Artefato que faz alguma coisa", "R$ 1.200,00"),
                listOf("Esse item é o terceiro", "Artefato que faz outra coisa", "R$ 1.500,00"),
                listOf("Acho que esse é o quarto", "Artefato que faz outra coisa", "R$ 1.500,00")
            ),
            columnWeights = listOf(1f, 1f, .5f)
        )
    }
}

@Composable
fun Table(
    headers: List<String>,
    rows: List<List<String>>,
    columnWeights: List<Float> = List(headers.size) { 1f },
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.border(
            width = 2.dp,
            color = MaterialTheme.colorScheme.outline
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Max)
        ) {
            headers.forEachIndexed { index, header ->
                Box(Modifier.weight(columnWeights[index])) {
                    Text(
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                        text = header,
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }

                if (index < headers.lastIndex) {
                    VerticalDivider(
                        modifier = Modifier.fillMaxHeight(),
                        thickness = 1.dp,
                        color = MaterialTheme.colorScheme.outline
                    )
                }
            }
        }

        rows.forEach { columns ->
            HorizontalDivider(
                modifier = Modifier.fillMaxWidth(),
                thickness = 1.dp,
                color = MaterialTheme.colorScheme.outline
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Max)
            ) {
                columns.forEachIndexed { index, text ->
                    Box(Modifier.weight(columnWeights[index])) {
                        Text(
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                            text = text,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    }

                    if (index < headers.lastIndex) {
                        VerticalDivider(
                            modifier = Modifier.fillMaxHeight(),
                            thickness = 1.dp,
                            color = MaterialTheme.colorScheme.outline
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalKoalaPlotApi::class)
@Composable
fun ExampleChart(modifier: Modifier = Modifier) {
    PieChart(
        modifier = modifier,
        values = List(6) { Random.nextInt(80, 3450).toFloat() },
        animationSpec = snap()
    )
}