package com.julia.imp.artifact.details

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.julia.imp.artifact.Artifact
import com.julia.imp.artifact.ArtifactInspectorList
import com.julia.imp.common.datetime.DateTimeFormats
import com.julia.imp.common.text.getInitials
import com.julia.imp.common.ui.avatar.Avatar
import com.julia.imp.common.ui.avatar.AvatarSize
import com.julia.imp.common.ui.dialog.ErrorDialog
import com.julia.imp.common.ui.title.CompoundTitle
import com.julia.imp.inspection.Inspection
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
import imp.composeapp.generated.resources.assignment_24px
import imp.composeapp.generated.resources.duration_format
import imp.composeapp.generated.resources.edit_24px
import imp.composeapp.generated.resources.inspect_label
import imp.composeapp.generated.resources.inspections_label
import imp.composeapp.generated.resources.inspectors_label
import imp.composeapp.generated.resources.inventory_2_24px
import imp.composeapp.generated.resources.last_inspection_label
import imp.composeapp.generated.resources.load_error_message
import imp.composeapp.generated.resources.load_error_title
import imp.composeapp.generated.resources.made_on_format
import imp.composeapp.generated.resources.never
import imp.composeapp.generated.resources.priority_label
import imp.composeapp.generated.resources.priority_wiegers_format
import imp.composeapp.generated.resources.select_inspector_label
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource
import kotlin.time.Duration.Companion.seconds

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArtifactDetailsScreen(
    artifact: Artifact,
    onBackClick: () -> Unit,
    onEditClick: () -> Unit,
    onInspectClick: () -> Unit,
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
        },
        floatingActionButton = {
            if (viewModel.uiState.canInspect) {
                ExtendedFloatingActionButton(
                    text = { Text(stringResource(Res.string.inspect_label)) },
                    icon = { Icon(vectorResource(Res.drawable.assignment_24px), null) },
                    onClick = onInspectClick
                )
            }
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
                    .consumeWindowInsets(PaddingValues(vertical = 24.dp))
                    .padding(paddingValues)
                    .padding(vertical = 24.dp)
                    .verticalScroll(rememberScrollState()),
                artifact = artifact,
                inspectors = viewModel.uiState.inspectors,
                onAddInspectorClick = { viewModel.openInspectorPicker() },
                onRemoveInspectorClick = { viewModel.removeInspector(it) },
                enableInspectorControls = !viewModel.uiState.updatingInspectors,
                lastInspection = viewModel.uiState.lastInspection,
                inspections = viewModel.uiState.inspections
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
    inspectors: List<User>,
    onAddInspectorClick: () -> Unit,
    onRemoveInspectorClick: (User) -> Unit,
    enableInspectorControls: Boolean,
    lastInspection: Instant?,
    inspections: List<Inspection>?,
    modifier: Modifier = Modifier
) {
    Column(modifier) {
        if (artifact.archived) {
            ArchivedArtifactAlert(
                modifier = Modifier
                    .padding(horizontal = 24.dp)
                    .padding(bottom = 24.dp)
            )
        }

        Text(
            modifier = Modifier
                .padding(horizontal = 24.dp)
                .padding(bottom = 4.dp),
            style = MaterialTheme.typography.labelMedium,
            text = stringResource(Res.string.artifact_name_label)
        )

        Text(
            modifier = Modifier.padding(horizontal = 24.dp),
            style = MaterialTheme.typography.bodyMedium,
            text = artifact.name
        )

        Text(
            modifier = Modifier
                .padding(horizontal = 24.dp)
                .padding(top = 24.dp, bottom = 4.dp),
            style = MaterialTheme.typography.labelMedium,
            text = stringResource(Res.string.artifact_type_label)
        )

        Text(
            modifier = Modifier.padding(horizontal = 24.dp),
            style = MaterialTheme.typography.bodyMedium,
            text = artifact.type.name
        )

        if (artifact.priority != null) {
            Text(
                modifier = Modifier
                    .padding(horizontal = 24.dp)
                    .padding(top = 24.dp, bottom = 4.dp),
                style = MaterialTheme.typography.labelMedium,
                text = stringResource(Res.string.priority_label)
            )

            Text(
                modifier = Modifier.padding(horizontal = 24.dp),
                style = MaterialTheme.typography.bodyMedium,
                text = getPriorityText(artifact.priority)
            )
        }

        Text(
            modifier = Modifier
                .padding(horizontal = 24.dp)
                .padding(top = 24.dp, bottom = 4.dp),
            style = MaterialTheme.typography.labelMedium,
            text = stringResource(Res.string.last_inspection_label)
        )

        Text(
            modifier = Modifier.padding(horizontal = 24.dp),
            style = MaterialTheme.typography.bodyMedium,
            text = lastInspection
                ?.toLocalDateTime(TimeZone.currentSystemDefault())
                ?.format(DateTimeFormats.DEFAULT)
                ?: stringResource(Res.string.never)
        )

        if (!artifact.archived) {
            Text(
                modifier = Modifier
                    .padding(horizontal = 24.dp)
                    .padding(top = 24.dp, bottom = 4.dp),
                style = MaterialTheme.typography.labelMedium,
                text = stringResource(Res.string.inspectors_label)
            )

            ArtifactInspectorList(
                modifier = Modifier
                    .padding(horizontal = 24.dp)
                    .fillMaxWidth(),
                inspectors = inspectors,
                onAddClick = onAddInspectorClick,
                onRemoveClick = onRemoveInspectorClick,
                enabled = enableInspectorControls
            )
        }

        if (!inspections.isNullOrEmpty()) {
            Text(
                modifier = Modifier
                    .padding(horizontal = 24.dp)
                    .padding(top = 24.dp, bottom = 12.dp),
                style = MaterialTheme.typography.labelMedium,
                text = stringResource(Res.string.inspections_label)
            )

            BoxWithConstraints(Modifier.fillMaxWidth()) {
                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(horizontal = 24.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(inspections) { inspection ->
                        val formattedDateTime = inspection.createdAt
                            .toLocalDateTime(TimeZone.currentSystemDefault())
                            .format(DateTimeFormats.DEFAULT)

                        ElevatedCard(
                            modifier = Modifier.widthIn(max = maxWidth - 48.dp),
                            onClick = {
                                // TODO: Redirect to inspection details
                            }
                        ) {
                            Column(Modifier.padding(24.dp)) {
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
                                        style = MaterialTheme.typography.titleMedium,
                                        color = MaterialTheme.colorScheme.onSurface,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                }

                                Text(
                                    modifier = Modifier.padding(top = 12.dp),
                                    text = stringResource(
                                        Res.string.made_on_format,
                                        formattedDateTime
                                    ),
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )

                                Text(
                                    modifier = Modifier.padding(top = 4.dp),
                                    text = stringResource(
                                        Res.string.duration_format,
                                        inspection.duration.inWholeSeconds.seconds.toString()
                                    ),
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }
                    }
                }
            }
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