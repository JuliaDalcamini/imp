package com.julia.imp.project.report

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.julia.imp.common.datetime.DateFormats
import com.julia.imp.common.datetime.sumOfDuration
import com.julia.imp.common.report.LabeledPieChart
import com.julia.imp.common.report.ReportField
import com.julia.imp.common.report.Table
import com.julia.imp.common.report.TableCell
import com.julia.imp.common.report.TableHeader
import com.julia.imp.common.report.TableRow
import com.julia.imp.common.text.formatAsCurrency
import com.julia.imp.priority.MoscowPrioritizer
import com.julia.imp.priority.WiegersPrioritizer
import com.julia.imp.project.Project
import com.julia.imp.project.dashboard.data.DashboardData
import com.julia.imp.project.dashboard.data.InspectorSummary
import imp.composeapp.generated.resources.Res
import imp.composeapp.generated.resources.assigned_inspections_label
import imp.composeapp.generated.resources.average_cost_per_artifact
import imp.composeapp.generated.resources.average_cost_per_inspection
import imp.composeapp.generated.resources.average_effort_per_artifact
import imp.composeapp.generated.resources.average_effort_per_inspection
import imp.composeapp.generated.resources.inspected_artifacts_label
import imp.composeapp.generated.resources.inspected_percentage_label
import imp.composeapp.generated.resources.inspections_made_label
import imp.composeapp.generated.resources.inspector_label
import imp.composeapp.generated.resources.inspectors_label
import imp.composeapp.generated.resources.min_inspectors_number_label
import imp.composeapp.generated.resources.moscow_label
import imp.composeapp.generated.resources.no_data_available
import imp.composeapp.generated.resources.overview_title
import imp.composeapp.generated.resources.owner_label
import imp.composeapp.generated.resources.percentage_format
import imp.composeapp.generated.resources.prioritization_method_label
import imp.composeapp.generated.resources.project_name_label
import imp.composeapp.generated.resources.quantity_with_percentage_format
import imp.composeapp.generated.resources.start_date_label
import imp.composeapp.generated.resources.target_date_label
import imp.composeapp.generated.resources.total_cost_label
import imp.composeapp.generated.resources.total_effort_label
import imp.composeapp.generated.resources.total_label
import imp.composeapp.generated.resources.total_of_artifacts_label
import imp.composeapp.generated.resources.wiegers_label
import kotlinx.datetime.format
import org.jetbrains.compose.resources.stringResource
import kotlin.time.Duration.Companion.seconds

@Composable
fun OverviewPage(
    data: DashboardData,
    project: Project,
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
            pageName = stringResource(Res.string.overview_title)
        )

        ProjectInfo(
            modifier = Modifier.fillMaxWidth(),
            project = project
        )

        HorizontalDivider(Modifier.fillMaxWidth())

        ProgressAndCostOverview(
            modifier = Modifier.fillMaxWidth(),
            data = data
        )

        HorizontalDivider(Modifier.fillMaxWidth())

        Text(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            text = stringResource(Res.string.inspectors_label),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center
        )

        if (data.inspectors.isNotEmpty()) {
            InspectorsTable(
                modifier = Modifier.fillMaxWidth(),
                data = data.inspectors
            )

            InspectorsCharts(
                modifier = Modifier.fillMaxWidth().height(180.dp),
                data = data.inspectors
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
private fun ProjectInfo(
    project: Project,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            ReportField(
                modifier = Modifier.weight(1f),
                label = stringResource(Res.string.project_name_label),
                value = project.name
            )

            ReportField(
                modifier = Modifier.weight(1f),
                label = stringResource(Res.string.prioritization_method_label),
                value = when (project.prioritizer) {
                    is MoscowPrioritizer -> stringResource(Res.string.moscow_label)
                    is WiegersPrioritizer -> stringResource(Res.string.wiegers_label)
                }
            )

            ReportField(
                modifier = Modifier.weight(1f),
                label = stringResource(Res.string.owner_label),
                value = project.creator.fullName
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            ReportField(
                modifier = Modifier.weight(1f),
                label = stringResource(Res.string.start_date_label),
                value = project.startDate.format(DateFormats.DEFAULT)
            )

            ReportField(
                modifier = Modifier.weight(1f),
                label = stringResource(Res.string.target_date_label),
                value = project.targetDate.format(DateFormats.DEFAULT)
            )

            ReportField(
                modifier = Modifier.weight(1f),
                label = stringResource(Res.string.min_inspectors_number_label),
                value = project.minInspectors.toString()
            )
        }
    }
}

@Composable
private fun ProgressAndCostOverview(
    data: DashboardData,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            val percentage = (data.inspectionProgress.percentage * 100).toInt()

            ReportField(
                modifier = Modifier.weight(1f),
                label = stringResource(Res.string.total_of_artifacts_label),
                value = data.inspectionProgress.total.toString()
            )

            ReportField(
                modifier = Modifier.weight(1f),
                label = stringResource(Res.string.inspected_artifacts_label),
                value = data.inspectionProgress.count.toString()
            )

            ReportField(
                modifier = Modifier.weight(1f),
                label = stringResource(Res.string.inspected_percentage_label),
                value = stringResource(Res.string.percentage_format, percentage)
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            ReportField(
                modifier = Modifier.weight(1f),
                label = stringResource(Res.string.total_cost_label),
                value = data.costOverview.total.formatAsCurrency()
            )

            ReportField(
                modifier = Modifier.weight(1f),
                label = stringResource(Res.string.average_cost_per_artifact),
                value = data.costOverview.averagePerArtifact.formatAsCurrency()
            )

            ReportField(
                modifier = Modifier.weight(1f),
                label = stringResource(Res.string.average_cost_per_inspection),
                value = data.costOverview.averagePerInspection.formatAsCurrency()
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            ReportField(
                modifier = Modifier.weight(1f),
                label = stringResource(Res.string.total_effort_label),
                value = data.effortOverview.total.inWholeSeconds.seconds.toString()
            )

            ReportField(
                modifier = Modifier.weight(1f),
                label = stringResource(Res.string.average_effort_per_artifact),
                value = data.effortOverview.averagePerArtifact.inWholeSeconds.seconds.toString()
            )

            ReportField(
                modifier = Modifier.weight(1f),
                label = stringResource(Res.string.average_effort_per_inspection),
                value = data.effortOverview.averagePerInspection.inWholeSeconds.seconds.toString()
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
        val totalEffort = data.sumOfDuration { it.totalEffort }
        val effortPercentages = data.map { (it.totalEffort / totalEffort).toFloat() }

        LabeledPieChart(
            modifier = Modifier.weight(1f),
            title = stringResource(Res.string.total_effort_label),
            values = effortPercentages,
            label = { index ->
                val summary = data[index]

                Text(summary.inspector.fullName)
                Text(summary.totalEffort.inWholeSeconds.seconds.toString())
            }
        )

        val totalAssigned = data.sumOf { it.progress.total }
        val assignedPercentages = data.map { (it.progress.total / totalAssigned).toFloat() }

        LabeledPieChart(
            modifier = Modifier.weight(1f),
            title = stringResource(Res.string.assigned_inspections_label),
            values = assignedPercentages,
            label = { index ->
                val summary = data[index]

                Text(summary.inspector.fullName)
                Text(summary.progress.total.toString())
            }
        )

        val totalDone = data.sumOf { it.progress.count }
        val donePercentages = data.map { (it.progress.count / totalDone).toFloat() }

        LabeledPieChart(
            modifier = Modifier.weight(1f),
            title = stringResource(Res.string.inspections_made_label),
            values = donePercentages,
            label = { index ->
                val summary = data[index]

                Text(summary.inspector.fullName)
                Text(summary.progress.count.toString())
            }
        )
    }
}