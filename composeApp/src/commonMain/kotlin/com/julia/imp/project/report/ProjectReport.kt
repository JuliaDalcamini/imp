package com.julia.imp.project.report

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.julia.imp.common.datetime.DateFormats
import com.julia.imp.common.report.ReportField
import com.julia.imp.common.report.ReportGenerator
import com.julia.imp.project.Project
import com.julia.imp.project.dashboard.data.DashboardData
import imp.composeapp.generated.resources.Res
import imp.composeapp.generated.resources.logo_48px
import imp.composeapp.generated.resources.project_report_title
import imp.composeapp.generated.resources.publish_date_label
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource

@Composable
fun ProjectReportGenerator(
    data: DashboardData,
    project: Project,
    onReportReady: (List<ImageBitmap>) -> Unit
) {
    ReportGenerator(
        { OverviewPage(modifier = Modifier.fillMaxSize(), data = data, project = project) },
        { InspectorsPage(modifier = Modifier.fillMaxSize(), data = data.inspectors) },
        { DefectTypesPage(modifier = Modifier.fillMaxSize(), data = data) },
        { ArtifactTypesPage(modifier = Modifier.fillMaxSize(), data = data.artifactTypes) },
        { ArtifactTypeDefectsPage(modifier = Modifier.fillMaxSize(), data = data.artifactTypes) },
        onReportReady = onReportReady
    )
}

@Composable
fun ProjectReportHeader(
    pageName: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Box(Modifier.weight(1f)) {
            Icon(
                imageVector = vectorResource(Res.drawable.logo_48px),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onBackground
            )
        }

        Column(
            verticalArrangement = Arrangement.spacedBy(2.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(Res.string.project_report_title),
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Center
            )

            Text(
                text = pageName,
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Center
            )
        }

        ReportField(
            modifier = Modifier.weight(1f),
            label = stringResource(Res.string.publish_date_label),
            value = Clock.System.now()
                .toLocalDateTime(TimeZone.currentSystemDefault())
                .date.format(DateFormats.DEFAULT),
            horizontalAlignment = Alignment.End,
            verticalAlignment = Alignment.CenterVertically
        )
    }
}