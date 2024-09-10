package com.julia.imp.report

import androidx.compose.animation.core.snap
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.rememberGraphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
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

        val rowContents = listOf(
            listOf("Item de teste número 1", "Artefato de inspeção de software", "R$ 1.000,00"),
            listOf("Item número 2", "Artefato que faz alguma coisa", "R$ 1.200,00"),
            listOf("Esse item é o terceiro", "Artefato que faz outra coisa", "R$ 1.500,00"),
            listOf("Acho que esse é o quarto", "Artefato que faz outra coisa", "R$ 1.500,00"),
            listOf("E esse é o quinto", "Artefato que faz outra coisa", "R$ 1.500,00"),
            listOf("Mas esse outro é o sexto", "Artefato que faz mais alguma outra coisa", "R$ 1.700,00"),
            listOf("Já esse aqui é o sétimo", "Artefato que faz uma coisa diferente", "R$ 1.860,00"),
            listOf("Esse eu acredito que seja o oitavo", "Será que esse artefato faz algo?", "R$ 102.563,00"),
            listOf("Artefato 9", "Esse aqui acho que é igual aos outros", "R$ 1.540,21"),
        )

        Table(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp)
        ) {
            val costColumnWeight = .5f

            TableRow {
                TableHeader("Nome")
                TableHeader("Tipo")

                TableHeader(
                    modifier = Modifier.weight(costColumnWeight),
                    text = "Valor"
                )
            }

            rowContents.forEach { row ->
                TableRow {
                    TableCell(row[0])
                    TableCell(row[1])

                    TableCell(
                        modifier = Modifier.weight(costColumnWeight),
                        text = row[2]
                    )
                }
            }
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

        val rowContents = listOf(
            listOf("Item de teste número 1", "Artefato de inspeção de software", "R$ 1.000,00"),
            listOf("Item número 2", "Artefato que faz alguma coisa", "R$ 1.200,00"),
            listOf("Esse item é o terceiro", "Artefato que faz outra coisa", "R$ 1.500,00"),
            listOf("Acho que esse é o quarto", "Artefato que faz outra coisa", "R$ 1.500,00")
        )

        Table(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp)
        ) {
            val typeHeaderWeight = 2f
            val costColumnWeight = .5f

            TableRow {
                TableHeader("Nome")

                TableHeader(
                    modifier = Modifier.weight(typeHeaderWeight),
                    text = "Tipo"
                )

                TableHeader(
                    modifier = Modifier.weight(costColumnWeight),
                    text = "Valor"
                )
            }

            rowContents.forEach { row ->
                TableRow {
                    TableCell(row[0])
                    TableCell(row[1])
                    TableCell(row[1])

                    TableCell(
                        modifier = Modifier.weight(costColumnWeight),
                        text = row[2]
                    )
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