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
import com.julia.imp.common.datetime.sumOfDuration
import com.julia.imp.common.report.LabeledPieChart
import com.julia.imp.common.report.Table
import com.julia.imp.common.report.TableCell
import com.julia.imp.common.report.TableHeader
import com.julia.imp.common.report.TableRow
import com.julia.imp.project.dashboard.data.InspectorSummary
import imp.composeapp.generated.resources.Res
import imp.composeapp.generated.resources.assigned_inspections_label
import imp.composeapp.generated.resources.inspections_made_label
import imp.composeapp.generated.resources.inspector_label
import imp.composeapp.generated.resources.inspectors_overview_title
import imp.composeapp.generated.resources.no_data_available
import imp.composeapp.generated.resources.quantity_with_percentage_format
import imp.composeapp.generated.resources.total_effort_label
import imp.composeapp.generated.resources.total_label
import org.jetbrains.compose.resources.stringResource
import kotlin.time.Duration.Companion.seconds

@Composable
fun InspectorsPage(
    data: List<InspectorSummary>,
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
            pageName = stringResource(Res.string.inspectors_overview_title)
        )

        if (data.isNotEmpty()) {
            InspectorsTable(
                modifier = Modifier.fillMaxWidth(),
                data = data
            )

            InspectorsCharts(
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
    }
}

@Composable
private fun InspectorsTable(
    data: List<InspectorSummary>,
    modifier: Modifier = Modifier
) {
    Table(modifier) {
        TableRow {
            TableHeader(stringResource(Res.string.inspector_label))

            TableHeader(
                modifier = Modifier.weight(.5f),
                text = stringResource(Res.string.total_effort_label)
            )

            TableHeader(
                modifier = Modifier.weight(.5f),
                text = stringResource(Res.string.assigned_inspections_label)
            )

            TableHeader(
                modifier = Modifier.weight(.5f),
                text = stringResource(Res.string.inspections_made_label)
            )
        }

        data.forEach { summary ->
            val percentage = (summary.progress.percentage * 100).toInt()

            TableRow {
                TableCell(summary.inspector.fullName)

                TableCell(
                    modifier = Modifier.weight(.5f),
                    textAlign = TextAlign.End,
                    text = summary.totalEffort.inWholeSeconds.seconds.toString()
                )

                TableCell(
                    modifier = Modifier.weight(.5f),
                    textAlign = TextAlign.End,
                    text = summary.progress.total.toString()
                )

                TableCell(
                    modifier = Modifier.weight(.5f),
                    textAlign = TextAlign.End,
                    text = stringResource(
                        Res.string.quantity_with_percentage_format,
                        summary.progress.count,
                        percentage
                    )
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
                text = data.sumOf { it.progress.total }.toString(),
                textStyle = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Bold
                )
            )

            TableCell(
                modifier = Modifier.weight(.5f),
                textAlign = TextAlign.End,
                text = stringResource(
                    Res.string.quantity_with_percentage_format,
                    data.sumOf { it.progress.count }.toString(),
                    (data.sumOf { it.progress.percentage } * 100).toInt()
                ),
                textStyle = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Bold
                )
            )
        }
    }
}

@Composable
private fun InspectorsCharts(
    data: List<InspectorSummary>,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        val effortData = data.filter { it.totalEffort.isPositive() }
        val totalEffort = effortData.sumOfDuration { it.totalEffort }
        val effortPercentages = effortData.map { (it.totalEffort / totalEffort).toFloat() }

        if (totalEffort.isPositive()) {
            LabeledPieChart(
                modifier = Modifier.weight(1f),
                title = stringResource(Res.string.total_effort_label),
                values = effortPercentages,
                label = { index ->
                    val summary = effortData[index]

                    Text(summary.inspector.fullName)
                    Text(summary.totalEffort.inWholeSeconds.seconds.toString())
                }
            )
        }

        val assignedData = data.filter { it.progress.total > 0 }
        val totalAssigned = assignedData.sumOf { it.progress.total }
        val assignedPercentages = assignedData.map { (it.progress.total / totalAssigned).toFloat() }

        if (totalAssigned > 0) {
            LabeledPieChart(
                modifier = Modifier.weight(1f),
                title = stringResource(Res.string.assigned_inspections_label),
                values = assignedPercentages,
                label = { index ->
                    val summary = assignedData[index]

                    Text(summary.inspector.fullName)
                    Text(summary.progress.total.toString())
                }
            )
        }

        val doneData = data.filter { it.progress.count > 0 }
        val totalDone = doneData.sumOf { it.progress.count }
        val donePercentages = doneData.map { (it.progress.count / totalDone).toFloat() }

        if (totalDone > 0) {
            LabeledPieChart(
                modifier = Modifier.weight(1f),
                title = stringResource(Res.string.inspections_made_label),
                values = donePercentages,
                label = { index ->
                    val summary = doneData[index]

                    Text(summary.inspector.fullName)
                    Text(summary.progress.count.toString())
                }
            )
        }
    }
}