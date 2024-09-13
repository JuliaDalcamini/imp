package com.julia.imp.inspection.details

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.julia.imp.artifact.Artifact
import com.julia.imp.common.datetime.DateTimeFormats
import com.julia.imp.common.text.formatAsCurrency
import com.julia.imp.common.text.getInitials
import com.julia.imp.common.ui.avatar.Avatar
import com.julia.imp.common.ui.avatar.AvatarSize
import com.julia.imp.common.ui.details.Property
import com.julia.imp.common.ui.details.TextProperty
import com.julia.imp.common.ui.dialog.ErrorDialog
import com.julia.imp.common.ui.padding.plus
import com.julia.imp.common.ui.topbar.TopBar
import com.julia.imp.inspection.Inspection
import com.julia.imp.inspection.answer.AnswerOption
import imp.composeapp.generated.resources.Res
import imp.composeapp.generated.resources.answer_format
import imp.composeapp.generated.resources.cost_title
import imp.composeapp.generated.resources.defect_format
import imp.composeapp.generated.resources.description_format
import imp.composeapp.generated.resources.duration_title
import imp.composeapp.generated.resources.found_defects_label
import imp.composeapp.generated.resources.inspection_details_title
import imp.composeapp.generated.resources.load_error_message
import imp.composeapp.generated.resources.load_error_title
import imp.composeapp.generated.resources.made_by_label
import imp.composeapp.generated.resources.made_on_label
import imp.composeapp.generated.resources.questions_and_answers_title
import imp.composeapp.generated.resources.summary_title
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.compose.resources.stringResource
import kotlin.time.Duration.Companion.seconds

@Composable
fun InspectionDetailsScreen(
    inspectionId: String,
    artifact: Artifact,
    projectId: String,
    onBackClick: () -> Unit,
    viewModel: InspectionDetailsViewModel = viewModel { InspectionDetailsViewModel() }
) {
    LaunchedEffect(inspectionId) {
        viewModel.initialize(
            inspectionId = inspectionId,
            artifactId = artifact.id,
            projectId = projectId
        )
    }

    Scaffold(
        modifier = Modifier.imePadding(),
        topBar = {
            TopBar(
                title = stringResource(Res.string.inspection_details_title),
                subtitle = artifact.name,
                onBackClick = onBackClick
            )
        }
    ) { paddingValues ->
        val uiState = viewModel.uiState

        if (uiState.loading) {
            Placeholder(
                modifier = Modifier
                    .fillMaxSize()
                    .consumeWindowInsets(paddingValues)
                    .consumeWindowInsets(PaddingValues(24.dp))
                    .padding(paddingValues)
                    .padding(24.dp)
            )
        }

        viewModel.uiState.inspection?.let { inspection ->
            InspectionDetails(
                modifier = Modifier
                    .fillMaxSize()
                    .consumeWindowInsets(paddingValues)
                    .consumeWindowInsets(PaddingValues(vertical = 24.dp)),
                contentPadding = paddingValues + 24.dp,
                inspection = inspection,
                showCosts = uiState.showCosts
            )
        }
    }

    if (viewModel.uiState.loadError) {
        ErrorDialog(
            title = stringResource(Res.string.load_error_title),
            message = stringResource(Res.string.load_error_message),
            onDismissRequest = {
                viewModel.dismissLoadError()
                onBackClick()
            }
        )
    }
}

@Composable
private fun Placeholder(modifier: Modifier = Modifier) {
    Box(modifier) {
        CircularProgressIndicator(Modifier.align(Alignment.Center))
    }
}

@Composable
fun InspectionDetails(
    inspection: Inspection,
    showCosts: Boolean,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp)
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = contentPadding
    ) {
        item("summaryHeader") {
            Text(
                modifier = Modifier.padding(bottom = 16.dp),
                text = stringResource(Res.string.summary_title),
                style = MaterialTheme.typography.titleMedium
            )
        }

        item("inspector") {
            Property(label = stringResource(Res.string.made_by_label)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Avatar(
                        initials = inspection.inspector.fullName.getInitials(),
                        size = AvatarSize.Small,
                    )

                    Text(
                        modifier = Modifier.padding(start = 8.dp),
                        text = inspection.inspector.fullName,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }

        item("dateTime") {
            TextProperty(
                modifier = Modifier.padding(top = 24.dp),
                label = stringResource(Res.string.made_on_label),
                text = inspection.createdAt
                    .toLocalDateTime(TimeZone.currentSystemDefault())
                    .format(DateTimeFormats.DEFAULT)
            )
        }

        item("duration") {
            TextProperty(
                modifier = Modifier.padding(top = 24.dp),
                label = stringResource(Res.string.duration_title),
                text = inspection.duration.inWholeSeconds.seconds.toString()
            )
        }

        if (showCosts) {
            item("cost") {
                TextProperty(
                    modifier = Modifier.padding(top = 24.dp),
                    label = stringResource(Res.string.cost_title),
                    text = inspection.cost.formatAsCurrency()
                )
            }
        }

        item("defectCount") {
            TextProperty(
                modifier = Modifier.padding(top = 24.dp),
                label = stringResource(Res.string.found_defects_label),
                text = inspection.answers.filter { it.answerOption == AnswerOption.No }.size.toString()
            )
        }

        item("questionsHeader") {
            Text(
                modifier = Modifier.padding(top = 24.dp, bottom = 16.dp),
                text = stringResource(Res.string.questions_and_answers_title),
                style = MaterialTheme.typography.titleMedium
            )
        }

        itemsIndexed(
            items = inspection.answers,
            key = { _, answer -> answer.id }
        ) { index, answer ->
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = answer.question.text,
                    style = MaterialTheme.typography.bodyMedium,
                )

                Text(
                    text = stringResource(Res.string.answer_format, answer.answerOption.getLabel()),
                    style = MaterialTheme.typography.labelMedium
                )

                answer.defect?.let { defect ->
                    Text(
                        text = stringResource(
                            Res.string.defect_format,
                            defect.type.name
                        ),
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.error
                    )

                    if (defect.description != null) {
                        Text(
                            text = stringResource(
                                Res.string.description_format,
                                defect.description
                            ),
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }
            }

            if (index < inspection.answers.lastIndex) {
                Spacer(Modifier.height(24.dp))
            }
        }
    }
}