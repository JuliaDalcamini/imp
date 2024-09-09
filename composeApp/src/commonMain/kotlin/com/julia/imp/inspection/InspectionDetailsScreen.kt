package com.julia.imp.inspection

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.julia.imp.common.datetime.DateTimeFormats
import com.julia.imp.common.text.formatAsCurrency
import com.julia.imp.common.text.getInitials
import com.julia.imp.common.ui.avatar.Avatar
import com.julia.imp.common.ui.avatar.AvatarSize
import com.julia.imp.common.ui.details.TextProperty
import com.julia.imp.common.ui.dialog.ErrorDialog
import com.julia.imp.common.ui.topbar.TopBar
import com.julia.imp.inspection.answer.AnswerOption
import imp.composeapp.generated.resources.Res
import imp.composeapp.generated.resources.cost_title
import imp.composeapp.generated.resources.detected_defect_format
import imp.composeapp.generated.resources.duration_title
import imp.composeapp.generated.resources.inspected_by_title
import imp.composeapp.generated.resources.inspection_details_title
import imp.composeapp.generated.resources.load_error_message
import imp.composeapp.generated.resources.load_error_title
import imp.composeapp.generated.resources.made_on_title
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.compose.resources.stringResource
import kotlin.time.Duration.Companion.seconds

@Composable
fun InspectionDetailsScreen(
    artifactId: String,
    projectId: String,
    inspectionId: String,
    onBackClick: () -> Unit,
    viewModel: InspectionDetailsViewModel = viewModel { InspectionDetailsViewModel() }
) {
    LaunchedEffect(artifactId) {
        viewModel.initialize(
            projectId = projectId,
            artifactId = artifactId,
            inspectionId = inspectionId
        )
    }

    Scaffold(
        modifier = Modifier.imePadding(),
        topBar = {
            TopBar(
                title = stringResource(Res.string.inspection_details_title),
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
                    .consumeWindowInsets(PaddingValues(vertical = 24.dp))
                    .padding(paddingValues)
                    .padding(vertical = 24.dp)
                    .verticalScroll(rememberScrollState()),
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
) {
    val formattedDateTime = inspection.createdAt
        .toLocalDateTime(TimeZone.currentSystemDefault())
        .format(DateTimeFormats.DEFAULT)

    Column(modifier) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Avatar(
                initials = inspection.inspector.fullName.getInitials(),
                size = AvatarSize.Small,
            )

            TextProperty(
                modifier = Modifier.padding(horizontal = 24.dp),
                label = stringResource(Res.string.inspected_by_title),
                text = inspection.inspector.fullName
            )

            TextProperty(
                modifier = Modifier
                    .padding(horizontal = 24.dp)
                    .padding(top = 24.dp),
                label = stringResource(Res.string.made_on_title),
                text = formattedDateTime
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextProperty(
                modifier = Modifier
                    .padding(horizontal = 24.dp)
                    .padding(top = 24.dp),
                label = stringResource(Res.string.duration_title),
                text = inspection.duration.inWholeSeconds.seconds.toString()
            )

            if (showCosts) {
                TextProperty(
                    modifier = Modifier
                        .padding(horizontal = 24.dp)
                        .padding(top = 24.dp),
                    label = stringResource(Res.string.cost_title),
                    text = inspection.cost.formatAsCurrency()
                )
            }
        }

        inspection.answers.forEach { answer ->
            TextProperty(
                modifier = Modifier
                    .padding(horizontal = 24.dp)
                    .padding(top = 24.dp),
                label = answer.question.text,
                text = mapAnswerToString(answer.answer)
            )

            if (answer.answer == AnswerOption.No) {
                Text(
                    modifier = Modifier
                        .padding(horizontal = 24.dp)
                        .padding(top = 8.dp),
                    text = stringResource(
                        Res.string.detected_defect_format,
                        answer.question.defectType.name
                    )
                )
            }
        }
    }
}

@Composable
fun mapAnswerToString(answer: AnswerOption): String {
    return when (answer) {
        AnswerOption.Yes -> "Sim"
        AnswerOption.No -> "Não"
        AnswerOption.NotApplicable -> "Não se Aplica"
    }
}