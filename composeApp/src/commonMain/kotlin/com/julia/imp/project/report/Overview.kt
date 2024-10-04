package com.julia.imp.project.report

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.julia.imp.common.datetime.DateFormats
import com.julia.imp.common.report.ReportField
import com.julia.imp.common.text.formatAsCurrency
import com.julia.imp.priority.MoscowPrioritizer
import com.julia.imp.priority.WiegersPrioritizer
import com.julia.imp.project.Project
import com.julia.imp.project.dashboard.data.DashboardData
import imp.composeapp.generated.resources.Res
import imp.composeapp.generated.resources.average_cost_per_artifact
import imp.composeapp.generated.resources.average_cost_per_inspection
import imp.composeapp.generated.resources.average_effort_per_artifact
import imp.composeapp.generated.resources.average_effort_per_inspection
import imp.composeapp.generated.resources.count_format
import imp.composeapp.generated.resources.inspected_artifacts_label
import imp.composeapp.generated.resources.inspected_percentage_label
import imp.composeapp.generated.resources.min_inspectors_number_label
import imp.composeapp.generated.resources.moscow_label
import imp.composeapp.generated.resources.overview_title
import imp.composeapp.generated.resources.owner_label
import imp.composeapp.generated.resources.percentage_format
import imp.composeapp.generated.resources.performance_score_label
import imp.composeapp.generated.resources.prioritization_method_label
import imp.composeapp.generated.resources.project_name_label
import imp.composeapp.generated.resources.start_date_label
import imp.composeapp.generated.resources.target_date_label
import imp.composeapp.generated.resources.total_cost_label
import imp.composeapp.generated.resources.total_effort_label
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
                label = stringResource(Res.string.performance_score_label),
                value = data.performanceScore.name
            )

            ReportField(
                modifier = Modifier.weight(1f),
                label = stringResource(Res.string.inspected_artifacts_label),
                value = stringResource(
                    Res.string.count_format,
                    data.inspectionProgress.count,
                    data.inspectionProgress.total
                )
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