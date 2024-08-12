package com.julia.imp.artifact.details

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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.julia.imp.artifact.Artifact
import com.julia.imp.artifact.ArtifactInspectorList
import com.julia.imp.common.ui.dialog.ErrorDialog
import com.julia.imp.common.ui.title.CompoundTitle
import com.julia.imp.priority.MoscowPriority
import com.julia.imp.priority.Priority
import com.julia.imp.priority.WiegersPriority
import com.julia.imp.user.User
import com.julia.imp.user.UserPickerDialog
import imp.composeapp.generated.resources.Res
import imp.composeapp.generated.resources.action_error_message
import imp.composeapp.generated.resources.action_error_title
import imp.composeapp.generated.resources.archived_artifact_alert_message
import imp.composeapp.generated.resources.arrow_back_24px
import imp.composeapp.generated.resources.artifact_details_title
import imp.composeapp.generated.resources.artifact_name_label
import imp.composeapp.generated.resources.artifact_type_label
import imp.composeapp.generated.resources.edit_24px
import imp.composeapp.generated.resources.inspectors_label
import imp.composeapp.generated.resources.inventory_2_24px
import imp.composeapp.generated.resources.last_inspection_label
import imp.composeapp.generated.resources.load_error_message
import imp.composeapp.generated.resources.load_error_title
import imp.composeapp.generated.resources.never
import imp.composeapp.generated.resources.priority_label
import imp.composeapp.generated.resources.priority_wiegers_format
import imp.composeapp.generated.resources.select_inspector_label
import kotlinx.datetime.Instant
import kotlinx.datetime.format
import kotlinx.datetime.format.DateTimeComponents
import kotlinx.datetime.format.char
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArtifactDetailsScreen(
    artifact: Artifact,
    onBackClick: () -> Unit,
    onEditClick: () -> Unit,
    viewModel: ArtifactDetailsViewModel = viewModel { ArtifactDetailsViewModel() }
) {
    LaunchedEffect(artifact) {
        viewModel.initialize(artifact)
    }

    Scaffold(
        modifier = Modifier.imePadding(),
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(vectorResource(Res.drawable.arrow_back_24px), null)
                    }
                },
                title = {
                    CompoundTitle(
                        title = stringResource(Res.string.artifact_details_title),
                        subtitle = artifact.name
                    )
                },
                actions = {
                    if (!artifact.archived) {
                        IconButton(onClick = onEditClick) {
                            Icon(vectorResource(Res.drawable.edit_24px), null)
                        }
                    }
                }
            )
        }
    ) { paddingValues ->
        if (viewModel.uiState.loading) {
            Placeholder(
                modifier = Modifier
                    .fillMaxSize()
                    .consumeWindowInsets(paddingValues)
                    .consumeWindowInsets(PaddingValues(24.dp))
                    .padding(paddingValues)
                    .padding(24.dp)
            )
        } else {
            ArtifactDetails(
                modifier = Modifier
                    .fillMaxSize()
                    .consumeWindowInsets(paddingValues)
                    .consumeWindowInsets(PaddingValues(24.dp))
                    .padding(paddingValues)
                    .padding(24.dp)
                    .verticalScroll(rememberScrollState()),
                artifact = artifact,
                lastInspection = viewModel.uiState.lastInspection,
                inspectors = viewModel.uiState.inspectors,
                onAddInspectorClick = { viewModel.openInspectorPicker() },
                onRemoveInspectorClick = { viewModel.removeInspector(it) },
                enableInspectorControls = !viewModel.uiState.updatingInspectors
            )
        }
    }

    if (viewModel.uiState.showInspectorPicker) {
        UserPickerDialog(
            title = stringResource(Res.string.select_inspector_label),
            availableUsers = viewModel.uiState.availableInspectors,
            onUserSelected = { viewModel.addInspector(it) },
            onDismissRequest = { viewModel.dismissInspectorPicker() }
        )
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

    if (viewModel.uiState.actionError) {
        ErrorDialog(
            title = stringResource(Res.string.action_error_title),
            message = stringResource(Res.string.action_error_message),
            onDismissRequest = { viewModel.dismissActionError() }
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
fun ArtifactDetails(
    artifact: Artifact,
    lastInspection: Instant?,
    inspectors: List<User>,
    onAddInspectorClick: () -> Unit,
    onRemoveInspectorClick: (User) -> Unit,
    enableInspectorControls: Boolean,
    modifier: Modifier = Modifier
) {
    Column(modifier) {
        if (artifact.archived) {
            ArchivedArtifactAlert(Modifier.padding(bottom = 24.dp))
        }

        Text(
            modifier = Modifier.padding(bottom = 4.dp),
            style = MaterialTheme.typography.labelMedium,
            text = stringResource(Res.string.artifact_name_label)
        )

        Text(
            style = MaterialTheme.typography.bodyMedium,
            text = artifact.name
        )

        Text(
            modifier = Modifier.padding(top = 24.dp, bottom = 4.dp),
            style = MaterialTheme.typography.labelMedium,
            text = stringResource(Res.string.artifact_type_label)
        )

        Text(
            style = MaterialTheme.typography.bodyMedium,
            text = artifact.type.name
        )

        Text(
            modifier = Modifier.padding(top = 24.dp, bottom = 4.dp),
            style = MaterialTheme.typography.labelMedium,
            text = stringResource(Res.string.priority_label)
        )

        Text(
            style = MaterialTheme.typography.bodyMedium,
            text = getPriorityText(artifact.priority)
        )

        Text(
            modifier = Modifier.padding(top = 24.dp, bottom = 4.dp),
            style = MaterialTheme.typography.labelMedium,
            text = stringResource(Res.string.last_inspection_label)
        )

        Text(
            style = MaterialTheme.typography.bodyMedium,
            text = lastInspection?.format(
                DateTimeComponents.Format {
                    dayOfMonth()
                    char('/')
                    monthNumber()
                    char('/')
                    year()
                    char(' ')
                    hour()
                    char(':')
                    minute()
                    char(':')
                    second()
                }
            ) ?: stringResource(Res.string.never)
        )

        if (!artifact.archived) {
            Text(
                modifier = Modifier.padding(top = 24.dp, bottom = 4.dp),
                style = MaterialTheme.typography.labelMedium,
                text = stringResource(Res.string.inspectors_label)
            )

            ArtifactInspectorList(
                modifier = Modifier.fillMaxWidth(),
                inspectors = inspectors,
                onAddClick = onAddInspectorClick,
                onRemoveClick = onRemoveInspectorClick,
                enabled = enableInspectorControls
            )
        }
    }
}

@Composable
private fun getPriorityText(priority: Priority) =
    when (priority) {
        is MoscowPriority -> priority.level.getLabel()
        is WiegersPriority -> getWiegersPriorityText(priority)
    }

@Composable
private fun getWiegersPriorityText(priority: WiegersPriority) =
    stringResource(
        Res.string.priority_wiegers_format,
        priority.userValue,
        priority.complexity,
        priority.impact
    )

@Composable
fun ArchivedArtifactAlert(modifier: Modifier = Modifier) {
    OutlinedCard(modifier) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                modifier = Modifier.padding(end = 16.dp),
                imageVector = vectorResource(Res.drawable.inventory_2_24px),
                contentDescription = null
            )

            Text(
                text = stringResource(Res.string.archived_artifact_alert_message),
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}