package com.julia.imp.project.report

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.julia.imp.common.report.LabeledPieChart
import com.julia.imp.common.report.Table
import com.julia.imp.common.report.TableCell
import com.julia.imp.common.report.TableHeader
import com.julia.imp.common.report.TableRow
import com.julia.imp.project.dashboard.data.ArtifactTypeSummary
import imp.composeapp.generated.resources.Res
import imp.composeapp.generated.resources.artifact_type_label
import imp.composeapp.generated.resources.defects_by_artifact_type_title
import imp.composeapp.generated.resources.defects_by_severity_label
import imp.composeapp.generated.resources.high_severity_label
import imp.composeapp.generated.resources.low_severity_label
import imp.composeapp.generated.resources.medium_severity_label
import imp.composeapp.generated.resources.no_data_available
import imp.composeapp.generated.resources.number_of_defects_label
import imp.composeapp.generated.resources.quantity_with_percentage_format
import imp.composeapp.generated.resources.total_label
import org.jetbrains.compose.resources.stringResource

@Composable
fun ArtifactTypeDefectsPage(
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
            pageName = stringResource(Res.string.defects_by_artifact_type_title)
        )

        if (data.isNotEmpty()) {
            ArtifactTypeDefectsTable(
                modifier = Modifier.fillMaxWidth(),
                data = data
            )
        } else {
            Text(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                text = stringResource(Res.string.no_data_available),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Center
            )
        }

        if (data.any { it.defects.total.count > 0 }) {
            ArtifactTypeDefectsCharts(
                modifier = Modifier.fillMaxWidth(),
                data = data
            )
        }
    }
}

@Composable
private fun ArtifactTypeDefectsTable(
    data: List<ArtifactTypeSummary>,
    modifier: Modifier = Modifier
) {
    Table(modifier) {
        TableRow {
            TableHeader(stringResource(Res.string.artifact_type_label))

            TableHeader(
                modifier = Modifier.weight(.5f),
                text = stringResource(Res.string.number_of_defects_label)
            )

            Column(modifier = Modifier.weight(1.5f)) {
                TableRow(Modifier.fillMaxWidth()) {
                    TableHeader(stringResource(Res.string.defects_by_severity_label))
                }

                TableRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) {
                    TableHeader(
                        modifier = Modifier.weight(.5f),
                        text = stringResource(Res.string.high_severity_label)
                    )

                    TableHeader(
                        modifier = Modifier.weight(.5f),
                        text = stringResource(Res.string.medium_severity_label)
                    )

                    TableHeader(
                        modifier = Modifier.weight(.5f),
                        text = stringResource(Res.string.low_severity_label)
                    )
                }
            }
        }

        data.forEach { summary ->
            val percentage = (summary.defects.percentage * 100).toInt()

            TableRow {
                TableCell(summary.artifactType.name)

                TableCell(
                    modifier = Modifier.weight(.5f),
                    textAlign = TextAlign.End,
                    text = stringResource(
                        Res.string.quantity_with_percentage_format,
                        summary.defects.total.count,
                        percentage
                    )
                )

                TableCell(
                    modifier = Modifier.weight(.5f),
                    textAlign = TextAlign.End,
                    text = summary.defects.highSeverity.count.toString()
                )

                TableCell(
                    modifier = Modifier.weight(.5f),
                    textAlign = TextAlign.End,
                    text = summary.defects.mediumSeverity.count.toString()
                )

                TableCell(
                    modifier = Modifier.weight(.5f),
                    textAlign = TextAlign.End,
                    text = summary.defects.lowSeverity.count.toString()
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
                text = data.sumOf { it.defects.total.count }.toString(),
                textStyle = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Bold
                )
            )

            TableCell(
                modifier = Modifier.weight(.5f),
                textAlign = TextAlign.End,
                text = data.sumOf { it.defects.highSeverity.count }.toString(),
                textStyle = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Bold
                )
            )

            TableCell(
                modifier = Modifier.weight(.5f),
                textAlign = TextAlign.End,
                text = data.sumOf { it.defects.mediumSeverity.count }.toString(),
                textStyle = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Bold
                )
            )

            TableCell(
                modifier = Modifier.weight(.5f),
                textAlign = TextAlign.End,
                text = data.sumOf { it.defects.lowSeverity.count }.toString(),
                textStyle = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Bold
                )
            )
        }
    }
}

@Composable
private fun ArtifactTypeDefectsCharts(
    data: List<ArtifactTypeSummary>,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        val highSeverityData = data.filter { it.defects.highSeverity.count > 0 }
        val highSeverityTotal = highSeverityData.sumOf { it.defects.highSeverity.count }

        if (highSeverityTotal > 0) {
            val highSeverityPercentages = highSeverityData.map {
                it.defects.highSeverity.count.toFloat() / highSeverityTotal
            }

            LabeledPieChart(
                modifier = Modifier.weight(1f),
                title = stringResource(Res.string.high_severity_label),
                values = highSeverityPercentages,
                label = { index ->
                    val summary = highSeverityData[index]

                    Text(summary.artifactType.name)
                    Text(summary.defects.highSeverity.count.toString())
                }
            )
        }

        val mediumSeverityData = data.filter { it.defects.mediumSeverity.count > 0 }
        val mediumSeverityTotal = mediumSeverityData.sumOf { it.defects.mediumSeverity.count }

        if (mediumSeverityTotal > 0) {
            val mediumSeverityPercentages = mediumSeverityData.map {
                it.defects.mediumSeverity.count.toFloat() / mediumSeverityTotal
            }

            LabeledPieChart(
                modifier = Modifier.weight(1f),
                title = stringResource(Res.string.medium_severity_label),
                values = mediumSeverityPercentages,
                label = { index ->
                    val summary = mediumSeverityData[index]

                    Text(summary.artifactType.name)
                    Text(summary.defects.mediumSeverity.count.toString())
                }
            )
        }

        val lowSeverityData = data.filter { it.defects.lowSeverity.count > 0 }
        val lowSeverityTotal = lowSeverityData.sumOf { it.defects.lowSeverity.count }

        if (lowSeverityTotal > 0) {
            val lowSeverityPercentages = lowSeverityData.map {
                it.defects.lowSeverity.count.toFloat() / lowSeverityTotal
            }

            LabeledPieChart(
                modifier = Modifier.weight(1f),
                title = stringResource(Res.string.low_severity_label),
                values = lowSeverityPercentages,
                label = { index ->
                    val summary = lowSeverityData[index]

                    Text(summary.artifactType.name)
                    Text(summary.defects.lowSeverity.count.toString())
                }
            )
        }
    }
}