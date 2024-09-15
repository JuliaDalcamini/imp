package com.julia.imp.project.dashboard

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.julia.imp.common.text.formatAsCurrency
import com.julia.imp.common.ui.padding.plus
import com.julia.imp.common.ui.topbar.TopBar
import com.julia.imp.project.Project
import com.julia.imp.project.dashboard.data.ArtifactTypeDefectSummary
import com.julia.imp.project.dashboard.data.CostOverview
import com.julia.imp.project.dashboard.data.DashboardData
import com.julia.imp.project.dashboard.data.DefectTypeSummary
import com.julia.imp.project.dashboard.data.EffortOverview
import com.julia.imp.project.dashboard.data.InspectorProgress
import com.julia.imp.project.dashboard.data.Progress
import imp.composeapp.generated.resources.Res
import imp.composeapp.generated.resources.average_by_inspected_artifact_label
import imp.composeapp.generated.resources.average_by_inspection_label
import imp.composeapp.generated.resources.average_cost_label
import imp.composeapp.generated.resources.average_effort_label
import imp.composeapp.generated.resources.cost_title
import imp.composeapp.generated.resources.count_format
import imp.composeapp.generated.resources.count_with_percentage_format
import imp.composeapp.generated.resources.dashboard_error_message
import imp.composeapp.generated.resources.defect_types_title
import imp.composeapp.generated.resources.defects_by_artifact_type_title
import imp.composeapp.generated.resources.effort_title
import imp.composeapp.generated.resources.fixed_defects_label
import imp.composeapp.generated.resources.fully_inspected_artifacts
import imp.composeapp.generated.resources.high_severity_label
import imp.composeapp.generated.resources.low_severity_label
import imp.composeapp.generated.resources.medium_severity_label
import imp.composeapp.generated.resources.no_defects_project_message
import imp.composeapp.generated.resources.overall_progress_title
import imp.composeapp.generated.resources.percentage_format
import imp.composeapp.generated.resources.progress_by_inspector_title
import imp.composeapp.generated.resources.project_stats_title
import imp.composeapp.generated.resources.quantity_label
import imp.composeapp.generated.resources.refresh_24px
import imp.composeapp.generated.resources.standard_deviation_label
import imp.composeapp.generated.resources.total_label
import imp.composeapp.generated.resources.try_again_label
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource
import kotlin.time.Duration.Companion.seconds

@Composable
fun DashboardScreen(
    project: Project,
    onBackClick: () -> Unit,
    viewModel: DashboardViewModel = viewModel { DashboardViewModel() }
) {
    LaunchedEffect(project) {
        viewModel.getDashboardData(project.id)
    }

    Scaffold(
        topBar = {
            TopBar(
                title = stringResource(Res.string.project_stats_title),
                subtitle = project.name,
                onBackClick = onBackClick
            )
        }
    ) { paddingValues ->
        Box(Modifier.fillMaxSize()) {
            if (viewModel.uiState.loading) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .consumeWindowInsets(paddingValues)
                        .padding(paddingValues)
                )
            }

            viewModel.uiState.data?.let { data ->
                val contentPadding = paddingValues + 16.dp

                DashboardContents(
                    modifier = Modifier
                        .fillMaxSize()
                        .consumeWindowInsets(contentPadding),
                    data = data,
                    contentPadding = contentPadding
                )
            }

            if (viewModel.uiState.error) {
                val padding = paddingValues + 24.dp

                ErrorMessage(
                    modifier = Modifier
                        .fillMaxSize()
                        .consumeWindowInsets(padding)
                        .padding(padding),
                    text = stringResource(Res.string.dashboard_error_message),
                    onRetryClick = { viewModel.getDashboardData(project.id) }
                )
            }
        }
    }
}

@Composable
fun DashboardContents(
    data: DashboardData,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp)
) {
    BoxWithConstraints(modifier) {
        val totalHorizontalPadding = contentPadding.run {
            calculateLeftPadding(LayoutDirection.Ltr) + calculateRightPadding(LayoutDirection.Ltr)
        }

        val columns = when {
            maxWidth >= 992.dp + totalHorizontalPadding -> 3
            maxWidth >= 656.dp + totalHorizontalPadding -> 2
            else -> 1
        }

        LazyVerticalStaggeredGrid(
            modifier = Modifier.fillMaxSize(),
            columns = StaggeredGridCells.Fixed(columns),
            contentPadding = contentPadding,
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalItemSpacing = 16.dp
        ) {
            item("progress") {
                ProjectProgressCard(
                    modifier = Modifier.fillMaxWidth(),
                    inspectionProgress = data.inspectionProgress,
                    defectsProgress = data.defectsProgress
                )
            }

            item("effort") {
                EffortOverviewCard(
                    modifier = Modifier.fillMaxWidth(),
                    data = data.effortOverview
                )
            }

            item("cost") {
                CostOverviewCard(
                    modifier = Modifier.fillMaxWidth(),
                    data = data.costOverview
                )
            }

            item("inspectors") {
                InspectorProgressCard(
                    modifier = Modifier.fillMaxWidth(),
                    data = data.inspectorsProgress
                )
            }

            item("defectTypes") {
                DefectTypesCard(
                    modifier = Modifier.fillMaxWidth(),
                    data = data.defectTypes
                )
            }

            item("artifactTypes") {
                ArtifactTypesCard(
                    modifier = Modifier.fillMaxWidth(),
                    data = data.artifactTypes
                )
            }
        }
    }
}

@Composable
fun ProjectProgressCard(
    inspectionProgress: Progress,
    defectsProgress: Progress,
    modifier: Modifier = Modifier
) {
    DashboardCard(
        modifier = modifier,
        title = stringResource(Res.string.overall_progress_title),
        itemSpacing = 12.dp
    ) {
        LabeledProgress(
            modifier = Modifier.fillMaxWidth(),
            label = stringResource(Res.string.fully_inspected_artifacts),
            progress = inspectionProgress
        )

        LabeledProgress(
            modifier = Modifier.fillMaxWidth(),
            label = stringResource(Res.string.fixed_defects_label),
            progress = defectsProgress
        )
    }
}

@Composable
fun LabeledProgress(
    label: String,
    progress: Progress,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        val percentage = (progress.percentage * 100).toInt()

        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium
        )

        LinearProgressIndicator(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            progress = { progress.percentage.toFloat() }
        )

        InfoRow(
            modifier = Modifier.fillMaxWidth(),
            label = stringResource(Res.string.count_format, progress.count, progress.total),
            text = stringResource(Res.string.percentage_format, percentage)
        )
    }
}

@Composable
fun EffortOverviewCard(
    data: EffortOverview,
    modifier: Modifier = Modifier
) {
    DashboardCard(
        modifier = modifier,
        title = stringResource(Res.string.effort_title)
    ) {
        InfoRow(
            modifier = Modifier.fillMaxWidth(),
            label = stringResource(Res.string.total_label),
            text = data.total.inWholeSeconds.seconds.toString()
        )

        InfoRow(
            modifier = Modifier.fillMaxWidth(),
            label = stringResource(Res.string.average_by_inspected_artifact_label),
            text = data.averagePerArtifact.inWholeSeconds.seconds.toString()
        )

        InfoRow(
            modifier = Modifier.fillMaxWidth(),
            label = stringResource(Res.string.average_by_inspection_label),
            text = data.averagePerInspection.inWholeSeconds.seconds.toString()
        )

        InfoRow(
            modifier = Modifier.fillMaxWidth(),
            label = stringResource(Res.string.standard_deviation_label),
            text = data.standardDeviationPerInspection.inWholeSeconds.seconds.toString()
        )
    }
}

@Composable
fun CostOverviewCard(
    data: CostOverview,
    modifier: Modifier = Modifier
) {
    DashboardCard(
        modifier = modifier,
        title = stringResource(Res.string.cost_title)
    ) {
        InfoRow(
            modifier = Modifier.fillMaxWidth(),
            label = stringResource(Res.string.total_label),
            text = data.total.formatAsCurrency()
        )

        InfoRow(
            modifier = Modifier.fillMaxWidth(),
            label = stringResource(Res.string.average_by_inspected_artifact_label),
            text = data.averagePerArtifact.formatAsCurrency()
        )

        InfoRow(
            modifier = Modifier.fillMaxWidth(),
            label = stringResource(Res.string.average_by_inspection_label),
            text = data.averagePerInspection.formatAsCurrency()
        )

        InfoRow(
            modifier = Modifier.fillMaxWidth(),
            label = stringResource(Res.string.standard_deviation_label),
            text = data.standardDeviationPerInspection.formatAsCurrency()
        )
    }
}

@Composable
fun InspectorProgressCard(
    data: List<InspectorProgress>,
    modifier: Modifier = Modifier
) {
    DashboardCard(
        modifier = modifier,
        title = stringResource(Res.string.progress_by_inspector_title)
    ) {
        data.forEach { progress ->
            val percentage = (progress.percentage * 100).toInt()

            InfoRow(
                modifier = Modifier.fillMaxWidth(),
                label = progress.inspector.fullName,
                text = stringResource(
                    Res.string.count_with_percentage_format,
                    progress.count,
                    progress.total,
                    percentage
                )
            )
        }
    }
}

@Composable
fun DefectTypesCard(
    data: List<DefectTypeSummary>,
    modifier: Modifier = Modifier
) {
    DashboardCard(
        modifier = modifier,
        title = stringResource(Res.string.defect_types_title),
        itemSpacing = 12.dp
    ) {
        data.forEach { summary ->
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                val percentage = (summary.percentage * 100).toInt()

                InfoRow(
                    modifier = Modifier.fillMaxWidth(),
                    label = summary.defectType.name,
                    text = stringResource(Res.string.percentage_format, percentage),
                    labelStyle = MaterialTheme.typography.labelLarge.copy(
                        color = MaterialTheme.colorScheme.primary
                    )
                )

                InfoRow(
                    modifier = Modifier.fillMaxWidth(),
                    label = stringResource(Res.string.quantity_label),
                    text = summary.count.toString()
                )

                InfoRow(
                    modifier = Modifier.fillMaxWidth(),
                    label = stringResource(Res.string.average_cost_label),
                    text = summary.averageCost.formatAsCurrency()
                )

                InfoRow(
                    modifier = Modifier.fillMaxWidth(),
                    label = stringResource(Res.string.average_effort_label),
                    text = summary.averageEffort.inWholeSeconds.seconds.toString()
                )
            }
        }

        if (data.isEmpty()) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(Res.string.no_defects_project_message),
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
fun ArtifactTypesCard(
    data: List<ArtifactTypeDefectSummary>,
    modifier: Modifier = Modifier
) {
    DashboardCard(
        modifier = modifier,
        title = stringResource(Res.string.defects_by_artifact_type_title),
        itemSpacing = 12.dp
    ) {
        data.forEach { summary ->
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                val percentage = (summary.percentage * 100).toInt()

                InfoRow(
                    modifier = Modifier.fillMaxWidth(),
                    label = summary.artifactType.name,
                    text = stringResource(Res.string.percentage_format, percentage),
                    labelStyle = MaterialTheme.typography.labelLarge.copy(
                        color = MaterialTheme.colorScheme.primary
                    )
                )

                InfoRow(
                    modifier = Modifier.fillMaxWidth(),
                    label = stringResource(Res.string.total_label),
                    text = summary.total.run { "$count (${cost.formatAsCurrency()})" }
                )

                InfoRow(
                    modifier = Modifier.fillMaxWidth(),
                    label = stringResource(Res.string.low_severity_label),
                    text = summary.lowSeverity.run { "$count (${cost.formatAsCurrency()})" }
                )

                InfoRow(
                    modifier = Modifier.fillMaxWidth(),
                    label = stringResource(Res.string.medium_severity_label),
                    text = summary.mediumSeverity.run { "$count (${cost.formatAsCurrency()})" }
                )

                InfoRow(
                    modifier = Modifier.fillMaxWidth(),
                    label = stringResource(Res.string.high_severity_label),
                    text = summary.highSeverity.run { "$count (${cost.formatAsCurrency()})" }
                )
            }
        }

        if (data.isEmpty()) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(Res.string.no_defects_project_message),
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
fun DashboardCard(
    title: String,
    modifier: Modifier = Modifier,
    itemSpacing: Dp = 4.dp,
    content: @Composable ColumnScope.() -> Unit
) {
    OutlinedCard(modifier) {
        Column(Modifier.fillMaxWidth().padding(16.dp)) {
            Text(
                modifier = Modifier.padding(bottom = 4.dp),
                text = title,
                style = MaterialTheme.typography.titleMedium
            )

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(itemSpacing),
                content = content
            )
        }
    }
}

@Composable
fun InfoRow(
    label: String,
    text: String,
    modifier: Modifier = Modifier,
    labelStyle: TextStyle = MaterialTheme.typography.bodyMedium,
    textStyle: TextStyle = MaterialTheme.typography.bodyMedium
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            modifier = Modifier.weight(1f),
            text = label,
            style = labelStyle
        )

        Text(
            modifier = Modifier.padding(start = 16.dp),
            text = text,
            style = textStyle
        )
    }
}

@Composable
fun ErrorMessage(
    text: String,
    onRetryClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center
        )

        TextButton(
            modifier = Modifier.padding(top = 4.dp),
            onClick = onRetryClick
        ) {
            Icon(vectorResource(Res.drawable.refresh_24px), null)
            Spacer(Modifier.width(8.dp))
            Text(stringResource(Res.string.try_again_label))
        }
    }
}