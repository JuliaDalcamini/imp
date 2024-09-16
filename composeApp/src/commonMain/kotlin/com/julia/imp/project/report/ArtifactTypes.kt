package com.julia.imp.project.report

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.julia.imp.common.datetime.sumOfDuration
import com.julia.imp.common.report.LabeledPieChart
import com.julia.imp.common.report.Table
import com.julia.imp.common.report.TableCell
import com.julia.imp.common.report.TableHeader
import com.julia.imp.common.report.TableRow
import com.julia.imp.common.text.formatAsCurrency
import com.julia.imp.project.dashboard.data.ArtifactTypeSummary
import imp.composeapp.generated.resources.Res
import imp.composeapp.generated.resources.artifact_type_label
import imp.composeapp.generated.resources.artifact_types_title
import imp.composeapp.generated.resources.total_cost_label
import imp.composeapp.generated.resources.total_effort_label
import imp.composeapp.generated.resources.total_label
import org.jetbrains.compose.resources.stringResource
import kotlin.time.Duration.Companion.seconds

@Composable
fun ArtifactTypesPage(
    data: List<ArtifactTypeSummary>,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        ProjectReportHeader(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp),
            pageName = stringResource(Res.string.artifact_types_title)
        )

        ArtifactTypesOverviewTable(
            modifier = Modifier.fillMaxWidth(),
            data = data
        )

        ArtifactTypesCharts(
            modifier = Modifier.fillMaxWidth().height(180.dp),
            data = data
        )
    }
}

@Composable
private fun ArtifactTypesOverviewTable(
    data: List<ArtifactTypeSummary>,
    modifier: Modifier = Modifier
) {
    Table(modifier) {
        TableRow {
            TableHeader(stringResource(Res.string.artifact_type_label))

            TableHeader(
                modifier = Modifier.weight(.5f),
                text = stringResource(Res.string.total_effort_label)
            )

            TableHeader(
                modifier = Modifier.weight(.5f),
                text = stringResource(Res.string.total_cost_label)
            )
        }

        data.forEach { summary ->
            TableRow {
                TableCell(summary.artifactType.name)

                TableCell(
                    modifier = Modifier.weight(.5f),
                    textAlign = TextAlign.End,
                    text = summary.totalEffort.inWholeSeconds.seconds.toString()
                )

                TableCell(
                    modifier = Modifier.weight(.5f),
                    textAlign = TextAlign.End,
                    text = summary.totalCost.formatAsCurrency()
                )
            }
        }

        TableRow {
            TableCell(
                text = stringResource(Res.string.total_label),
                textStyle = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Bold
                )
            )

            TableCell(
                modifier = Modifier.weight(.5f),
                textAlign = TextAlign.End,
                text = data.sumOfDuration { it.totalEffort }.inWholeSeconds.seconds.toString(),
                textStyle = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Bold
                )
            )

            TableCell(
                modifier = Modifier.weight(.5f),
                textAlign = TextAlign.End,
                text = data.sumOf { it.totalCost }.formatAsCurrency(),
                textStyle = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Bold
                )
            )
        }
    }
}

@Composable
private fun ArtifactTypesCharts(
    data: List<ArtifactTypeSummary>,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        val totalEffort = data.sumOfDuration { it.totalEffort }
        val effortPercentages = data.map { (it.totalEffort / totalEffort).toFloat() }

        LabeledPieChart(
            modifier = Modifier.weight(1f),
            title = stringResource(Res.string.total_effort_label),
            values = effortPercentages,
            label = { index ->
                val summary = data[index]

                Text(summary.artifactType.name)
                Text(summary.totalEffort.inWholeSeconds.seconds.toString())
            }
        )

        val totalCost = data.sumOf { it.totalCost }
        val costPercentages = data.map { (it.totalCost / totalCost).toFloat() }

        LabeledPieChart(
            modifier = Modifier.weight(1f),
            title = stringResource(Res.string.total_cost_label),
            values = costPercentages,
            label = { index ->
                val summary = data[index]

                Text(summary.artifactType.name)
                Text(summary.totalCost.formatAsCurrency())
            }
        )
    }
}