package com.julia.imp.project.list

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material.icons.outlined.Warning
import androidx.compose.material3.AlertDialog
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.julia.imp.common.session.SessionManager
import com.julia.imp.common.ui.dialog.RenameDialog
import com.julia.imp.project.Project
import com.julia.imp.team.switcher.TeamSwitcher
import imp.composeapp.generated.resources.Res
import imp.composeapp.generated.resources.cancel_label
import imp.composeapp.generated.resources.new_project_label
import imp.composeapp.generated.resources.created_by_format
import imp.composeapp.generated.resources.delete_label
import imp.composeapp.generated.resources.delete_project_message
import imp.composeapp.generated.resources.delete_project_title
import imp.composeapp.generated.resources.no_projects_message
import imp.composeapp.generated.resources.ok_label
import imp.composeapp.generated.resources.projects_error_message
import imp.composeapp.generated.resources.projects_title
import imp.composeapp.generated.resources.rename_label
import imp.composeapp.generated.resources.switch_team_title
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun ProjectsScreen(
    onProjectClick: (Project) -> Unit,
    onNewProjectClick: () -> Unit,
    viewModel: ProjectsViewModel = viewModel { ProjectsViewModel() }
) {
    LaunchedEffect(SessionManager.activeSession) {
        viewModel.getProjects()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(Res.string.projects_title)) },
                actions = { TeamSwitcher() }
            )
        },
        floatingActionButton = {
            if (SessionManager.activeSession?.isTeamAdmin == true) {
                NewProjectButton(onClick = onNewProjectClick)
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

            viewModel.uiState.projects?.let { projects ->
                if (projects.isNotEmpty()) {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .consumeWindowInsets(paddingValues),
                        contentPadding = paddingValues
                    ) {
                        items(projects) { project ->
                            ProjectListItem(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp)
                                    .padding(bottom = 8.dp),
                                project = project,
                                onClick = { onProjectClick(project) },
                                onRenameClick = { viewModel.askToRenameProject(project) },
                                onDeleteClick = { viewModel.askToDeleteProject(project) }
                            )
                        }

                        item {
                            Spacer(Modifier.height(56.dp))
                        }
                    }
                } else {
                    Text(
                        modifier = Modifier.padding(24.dp).align(Alignment.Center),
                        text = stringResource(Res.string.no_projects_message)
                    )
                }
            }

            viewModel.uiState.error?.let {
                Text(
                    modifier = Modifier
                        .padding(paddingValues)
                        .padding(24.dp)
                        .align(Alignment.Center),
                    text = stringResource(Res.string.projects_error_message)
                )
            }

            viewModel.uiState.projectToDelete?.let { project ->
                DeleteProjectDialog(
                    projectName = project.name,
                    onDismissRequest = { viewModel.dismissProjectDeletion() },
                    onConfirm = { viewModel.deleteProject(project) }
                )
            }

            viewModel.uiState.projectToRename?.let { project ->
                RenameProjectDialog(
                    projectName = project.name,
                    onDismissRequest = { viewModel.dismissProjectRenaming() },
                    onConfirm = { newName -> viewModel.renameProject(project, newName) }
                )
            }
        }
    }
}

@Composable
fun NewProjectButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    ExtendedFloatingActionButton(
        modifier = modifier,
        text = { Text(stringResource(Res.string.new_project_label)) },
        icon = { Icon(Icons.Default.Add, null) },
        onClick = onClick
    )
}

@Composable
fun ProjectListItem(
    project: Project,
    onClick: () -> Unit,
    onRenameClick: () -> Unit,
    onDeleteClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    ElevatedCard(
        modifier = modifier,
        onClick = onClick,
    ) {
        Row(
            modifier = Modifier.padding(24.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(Modifier.weight(1f)) {
                Text(
                    text = project.name,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    modifier = Modifier.padding(top = 4.dp),
                    text = stringResource(Res.string.created_by_format, project.creator.fullName),
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

                ProjectOptionsDropdown(
                    expanded = showOptions,
                    onDismissRequest = { showOptions = false },
                    onRenameClick = onRenameClick,
                    onDeleteClick = onDeleteClick
                )
            }
        }
    }
}

@Composable
fun ProjectOptionsDropdown(
    expanded: Boolean,
    onDismissRequest: () -> Unit,
    onRenameClick: () -> Unit,
    onDeleteClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    DropdownMenu(
        modifier = modifier,
        expanded = expanded,
        onDismissRequest = onDismissRequest
    ) {
        DropdownMenuItem(
            text = { Text(stringResource(Res.string.rename_label)) },
            onClick = {
                onRenameClick()
                onDismissRequest()
            }
        )

        DropdownMenuItem(
            text = { Text(stringResource(Res.string.delete_label)) },
            onClick = {
                onDeleteClick()
                onDismissRequest()
            }
        )
    }
}

@Composable
fun RenameProjectDialog(
    projectName: String,
    onDismissRequest: () -> Unit,
    onConfirm: (String) -> Unit
) {
    RenameDialog(
        title = stringResource(Res.string.switch_team_title),
        initialValue = projectName,
        onDismissRequest = onDismissRequest,
        onConfirm = onConfirm
    )
}

@Composable
fun DeleteProjectDialog(
    projectName: String,
    onDismissRequest: () -> Unit,
    onConfirm: () -> Unit
) {
    AlertDialog(
        icon = { Icon(Icons.Outlined.Warning, null) },
        title = { Text(text = stringResource(Res.string.delete_project_title)) },
        text = { Text(text = stringResource(Res.string.delete_project_message, projectName)) },
        onDismissRequest = onDismissRequest,
        dismissButton = {
            TextButton(onClick = onDismissRequest) {
                Text(stringResource(Res.string.cancel_label))
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirm()
                    onDismissRequest()
                }
            ) {
                Text(stringResource(Res.string.ok_label))
            }
        }
    )
}