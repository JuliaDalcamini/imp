package com.julia.imp.artifact.list

import androidx.compose.foundation.ExperimentalFoundationApi
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.julia.imp.artifact.Artifact
import com.julia.imp.common.ui.dialog.ConfirmationDialog
import com.julia.imp.common.ui.dialog.ErrorDialog
import com.julia.imp.priority.MoscowPriority
import com.julia.imp.priority.Priority
import com.julia.imp.priority.WiegersPriority
import imp.composeapp.generated.resources.Res
import imp.composeapp.generated.resources.action_error_message
import imp.composeapp.generated.resources.action_error_title
import imp.composeapp.generated.resources.add_24px
import imp.composeapp.generated.resources.archive_24px
import imp.composeapp.generated.resources.archive_artifact_message
import imp.composeapp.generated.resources.archive_artifact_title
import imp.composeapp.generated.resources.archive_label
import imp.composeapp.generated.resources.archived_label
import imp.composeapp.generated.resources.arrow_back_24px
import imp.composeapp.generated.resources.artifacts_error_message
import imp.composeapp.generated.resources.artifacts_title
import imp.composeapp.generated.resources.edit_24px
import imp.composeapp.generated.resources.edit_label
import imp.composeapp.generated.resources.filter_active
import imp.composeapp.generated.resources.filter_all
import imp.composeapp.generated.resources.filter_archived
import imp.composeapp.generated.resources.filter_assigned_to_me
import imp.composeapp.generated.resources.inventory_2_20px
import imp.composeapp.generated.resources.more_vert_24px
import imp.composeapp.generated.resources.new_artifact_label
import imp.composeapp.generated.resources.no_artifacts_message
import imp.composeapp.generated.resources.priority_moscow_format
import imp.composeapp.generated.resources.priority_wiegers_format
import imp.composeapp.generated.resources.refresh_24px
import imp.composeapp.generated.resources.try_again_label
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArtifactsScreen(
    projectId: String,
    onBackClick: () -> Unit,
    onNewArtifactClick: () -> Unit,
    onEditArtifactClick: (Artifact) -> Unit,
    viewModel: ArtifactsViewModel = viewModel { ArtifactsViewModel() }
) {
    LaunchedEffect(viewModel.uiState.filter) {
        viewModel.getArtifacts(projectId, viewModel.uiState.filter)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(Res.string.artifacts_title)) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(vectorResource(Res.drawable.arrow_back_24px), null)
                    }
                }
            )
        },
        floatingActionButton = {
            if (viewModel.uiState.showCreateButton) {
                NewArtifactButton(onClick = onNewArtifactClick)
            }
        }
    ) { paddingValues ->
        Box(Modifier.fillMaxSize()) {
            if (viewModel.uiState.loading) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(paddingValues)
                )
            }

            ArtifactList(
                modifier = Modifier
                    .consumeWindowInsets(paddingValues)
                    .fillMaxSize(),
                contentPadding = paddingValues,
                entries = viewModel.uiState.entries,
                onEditArtifactClick = onEditArtifactClick,
                onArchiveArtifactClick = { viewModel.askToArchive(it) },
                selectedFilter = viewModel.uiState.filter,
                onFilterChange = { viewModel.setFilter(it) }
            )

            if (viewModel.uiState.error) {
                Column(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = stringResource(Res.string.artifacts_error_message),
                        style = MaterialTheme.typography.bodyLarge,
                        textAlign = TextAlign.Center
                    )

                    TextButton(
                        modifier = Modifier.padding(top = 4.dp),
                        onClick = { viewModel.reload() }
                    ) {
                        Icon(vectorResource(Res.drawable.refresh_24px), null)
                        Spacer(Modifier.width(8.dp))
                        Text(stringResource(Res.string.try_again_label))
                    }
                }
            }

            if (viewModel.uiState.empty) {
                Text(
                    modifier = Modifier.padding(24.dp).align(Alignment.Center),
                    text = stringResource(Res.string.no_artifacts_message)
                )
            }
        }
    }

    viewModel.uiState.artifactToArchive?.let { artifact ->
        ArchiveArtifactDialog(
            artifactName = artifact.name,
            onDismissRequest = { viewModel.dismissArchiving() },
            onConfirm = { viewModel.archive(artifact) }
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
fun ArchiveArtifactDialog(
    artifactName: String,
    onDismissRequest: () -> Unit,
    onConfirm: () -> Unit
) {
    ConfirmationDialog(
        title = stringResource(Res.string.archive_artifact_title),
        message = stringResource(Res.string.archive_artifact_message, artifactName),
        onDismissRequest = onDismissRequest,
        onConfirm = onConfirm
    )
}

@Composable
fun NewArtifactButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    ExtendedFloatingActionButton(
        modifier = modifier,
        text = { Text(stringResource(Res.string.new_artifact_label)) },
        icon = { Icon(vectorResource(Res.drawable.add_24px), null) },
        onClick = onClick
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ArtifactList(
    entries: List<ArtifactListEntry>?,
    onEditArtifactClick: (Artifact) -> Unit,
    onArchiveArtifactClick: (Artifact) -> Unit,
    selectedFilter: ArtifactFilter,
    onFilterChange: (ArtifactFilter) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues()
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = contentPadding
    ) {
        stickyHeader("filters") {
            ArtifactListFilters(
                modifier = Modifier.fillMaxWidth(),
                selectedFilter = selectedFilter,
                onFilterChange = onFilterChange
            )
        }

        entries?.let {
            items(entries) { entry ->
                ArtifactListItem(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .padding(top = 8.dp)
                        .fillMaxWidth()
                        .animateItem(),
                    artifact = entry.artifact,
                    showOptions = entry.showOptions,
                    onEditClick = { onEditArtifactClick(entry.artifact) },
                    onArchiveClick = { onArchiveArtifactClick(entry.artifact) }
                )
            }
        }

        item {
            Spacer(Modifier.height(56.dp))
        }
    }
}

@Composable
fun ArtifactListFilters(
    modifier: Modifier,
    selectedFilter: ArtifactFilter,
    onFilterChange: (ArtifactFilter) -> Unit
) {
    LazyRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(horizontal = 16.dp)
    ) {
        items(ArtifactFilter.entries) {
            FilterChip(
                selected = it == selectedFilter,
                onClick = { onFilterChange(it) },
                label = { Text(getFilterText(it)) }
            )
        }
    }
}

@Composable
private fun getFilterText(filter: ArtifactFilter): String =
    when (filter) {
        ArtifactFilter.Active -> stringResource(Res.string.filter_active)
        ArtifactFilter.AssignedToMe -> stringResource(Res.string.filter_assigned_to_me)
        ArtifactFilter.Archived -> stringResource(Res.string.filter_archived)
        ArtifactFilter.All -> stringResource(Res.string.filter_all)
    }

@Composable
fun ArtifactListItem(
    artifact: Artifact,
    showOptions: Boolean,
    onEditClick: () -> Unit,
    onArchiveClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    ElevatedCard(modifier) {
        Row(
            modifier = Modifier.padding(24.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val contentModifier =
                if (artifact.archived) Modifier.alpha(.5f)
                else Modifier

            Column(contentModifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = artifact.name,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    if (artifact.archived) {
                        Icon(
                            modifier = Modifier.size(16.dp),
                            imageVector = vectorResource(Res.drawable.inventory_2_20px),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }

                Text(
                    modifier = Modifier.padding(top = 4.dp),
                    text = artifact.type.name,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                if (artifact.archived) {
                    Text(
                        modifier = Modifier.padding(top = 4.dp),
                        text = stringResource(Res.string.archived_label),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                } else {
                    Text(
                        modifier = Modifier.padding(top = 4.dp),
                        text = getPriorityText(artifact.priority),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            if (showOptions) {
                Box {
                    var expandOptions by remember { mutableStateOf(false) }

                    IconButton(onClick = { expandOptions = true }) {
                        Icon(vectorResource(Res.drawable.more_vert_24px), null)
                    }

                    ArtifactOptionsDropdown(
                        expanded = expandOptions,
                        onDismissRequest = { expandOptions = false },
                        onEditClick = onEditClick,
                        onArchiveClick = onArchiveClick
                    )
                }
            }
        }
    }
}

@Composable
private fun getPriorityText(priority: Priority) =
    when (priority) {
        is MoscowPriority -> stringResource(Res.string.priority_moscow_format, priority.level.getLabel())
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
fun ArtifactOptionsDropdown(
    expanded: Boolean,
    onDismissRequest: () -> Unit,
    onEditClick: () -> Unit,
    onArchiveClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    DropdownMenu(
        modifier = modifier,
        expanded = expanded,
        onDismissRequest = onDismissRequest
    ) {
        DropdownMenuItem(
            text = { Text(stringResource(Res.string.edit_label)) },
            leadingIcon = { Icon(vectorResource(Res.drawable.edit_24px), null) },
            onClick = {
                onEditClick()
                onDismissRequest()
            }
        )

        DropdownMenuItem(
            text = { Text(stringResource(Res.string.archive_label)) },
            leadingIcon = { Icon(vectorResource(Res.drawable.archive_24px), null) },
            onClick = {
                onArchiveClick()
                onDismissRequest()
            }
        )
    }
}