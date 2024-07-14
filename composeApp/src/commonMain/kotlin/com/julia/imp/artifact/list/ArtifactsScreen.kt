package com.julia.imp.artifact.list

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.julia.imp.common.ui.dialog.ConfirmationDialog
import com.julia.imp.priority.MoscowPriority
import com.julia.imp.priority.MoscowPriorityLevel
import com.julia.imp.priority.Priority
import com.julia.imp.priority.WiegersPriority
import imp.composeapp.generated.resources.Res
import imp.composeapp.generated.resources.archive_artifact_message
import imp.composeapp.generated.resources.archive_artifact_title
import imp.composeapp.generated.resources.archive_label
import imp.composeapp.generated.resources.artifacts_title
import imp.composeapp.generated.resources.edit_label
import imp.composeapp.generated.resources.new_artifact_label
import imp.composeapp.generated.resources.priority_moscow_could_have
import imp.composeapp.generated.resources.priority_moscow_must_have
import imp.composeapp.generated.resources.priority_moscow_should_have
import imp.composeapp.generated.resources.priority_moscow_wont_have
import imp.composeapp.generated.resources.priority_wiegers_format
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArtifactsScreen(
    projectId: String,
    onBackClick: () -> Unit,
    onNewArtifactClick: () -> Unit,
    onEditArtifactClick: (ArtifactListEntry) -> Unit,
    viewModel: ArtifactsViewModel = viewModel { ArtifactsViewModel() }
) {
    LaunchedEffect(Unit) {
        viewModel.getArtifacts(projectId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(Res.string.artifacts_title)) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Outlined.ArrowBack, null)
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
            if (viewModel.uiState.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(paddingValues)
                )
            }

            viewModel.uiState.artifacts?.let { artifacts ->
                ArtifactList(
                    modifier = Modifier
                        .consumeWindowInsets(paddingValues)
                        .fillMaxSize(),
                    contentPadding = paddingValues,
                    artifacts = artifacts,
                    onEditArtifactClick = onEditArtifactClick,
                    onArchiveArtifactClick = { viewModel.askToArchive(it) }
                )
            }
        }

        viewModel.uiState.artifactToArchive?.let { artifact ->
            ArchiveArtifactDialog(
                artifactName = artifact.name,
                onDismissRequest = { viewModel.dismissArchiving() },
                onConfirm = { viewModel.archive(artifact) }
            )
        }
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
        icon = { Icon(Icons.Default.Add, null) },
        onClick = onClick
    )
}

@Composable
fun ArtifactList(
    artifacts: List<ArtifactListEntry>,
    onEditArtifactClick: (ArtifactListEntry) -> Unit,
    onArchiveArtifactClick: (ArtifactListEntry) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues()
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = contentPadding
    ) {
        items(artifacts) { artifact ->
            ArtifactListItem(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 8.dp)
                    .fillMaxWidth()
                    .animateItem(),
                artifact = artifact,
                onEditClick = { onEditArtifactClick(artifact) },
                onArchiveClick = { onArchiveArtifactClick(artifact) }
            )
        }

        item {
            Spacer(Modifier.height(56.dp))
        }
    }
}

@Composable
fun ArtifactListItem(
    artifact: ArtifactListEntry,
    onEditClick: () -> Unit,
    onArchiveClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    ElevatedCard(modifier) {
        Row(
            modifier = Modifier.padding(24.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(Modifier.weight(1f)) {
                Text(
                    text = artifact.name,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    modifier = Modifier.padding(top = 4.dp),
                    text = artifact.type.name,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    modifier = Modifier.padding(top = 4.dp),
                    text = getPriorityText(artifact.priority),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Box {
                var showOptions by remember { mutableStateOf(false) }

                IconButton(onClick = { showOptions = true }) {
                    Icon(Icons.Outlined.MoreVert, null)
                }

                ArtifactOptionsDropdown(
                    expanded = showOptions,
                    onDismissRequest = { showOptions = false },
                    onEditClick = onEditClick,
                    onArchiveClick = onArchiveClick
                )
            }
        }
    }
}

@Composable
private fun getPriorityText(priority: Priority) =
    when (priority) {
        is MoscowPriority -> getMoscowPriorityText(priority)
        is WiegersPriority -> getWiegersPriorityText(priority)
    }

@Composable
private fun getMoscowPriorityText(priority: MoscowPriority) =
    when (priority.level) {
        MoscowPriorityLevel.WontHave -> stringResource(Res.string.priority_moscow_wont_have)
        MoscowPriorityLevel.CouldHave -> stringResource(Res.string.priority_moscow_could_have)
        MoscowPriorityLevel.ShouldHave -> stringResource(Res.string.priority_moscow_should_have)
        MoscowPriorityLevel.MustHave -> stringResource(Res.string.priority_moscow_must_have)
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
            leadingIcon = { Icon(Icons.Outlined.Edit, null) },
            onClick = {
                onEditClick()
                onDismissRequest()
            }
        )

        DropdownMenuItem(
            text = { Text(stringResource(Res.string.archive_label)) },
            leadingIcon = { Icon(Icons.Outlined.Lock, null) },
            onClick = {
                onArchiveClick()
                onDismissRequest()
            }
        )
    }
}