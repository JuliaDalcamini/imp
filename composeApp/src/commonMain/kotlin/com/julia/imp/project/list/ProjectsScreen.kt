package com.julia.imp.project.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material.icons.outlined.Refresh
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
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.julia.imp.common.session.SessionManager
import com.julia.imp.common.session.UserSession
import com.julia.imp.common.ui.dialog.ConfirmationDialog
import com.julia.imp.common.ui.dialog.ErrorDialog
import com.julia.imp.common.ui.dialog.RenameDialog
import com.julia.imp.project.Project
import com.julia.imp.report.ProjectReportGenerator
import com.julia.imp.team.switcher.TeamSwitcher
import imp.composeapp.generated.resources.Res
import imp.composeapp.generated.resources.action_error_message
import imp.composeapp.generated.resources.action_error_title
import imp.composeapp.generated.resources.created_by_format
import imp.composeapp.generated.resources.delete_label
import imp.composeapp.generated.resources.delete_project_message
import imp.composeapp.generated.resources.delete_project_title
import imp.composeapp.generated.resources.generate_report_label
import imp.composeapp.generated.resources.new_project_label
import imp.composeapp.generated.resources.no_projects_message
import imp.composeapp.generated.resources.projects_error_message
import imp.composeapp.generated.resources.projects_title
import imp.composeapp.generated.resources.rename_label
import imp.composeapp.generated.resources.switch_team_title
import imp.composeapp.generated.resources.try_again_label
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProjectsScreen(
    onNewProjectClick: () -> Unit,
    onProjectClick: (Project) -> Unit,
    onTeamSwitch: (UserSession) -> Unit,
    onCreateTeamClick: () -> Unit,
    onShowReportRequest: (List<ImageBitmap>) -> Unit,
    viewModel: ProjectsViewModel = viewModel { ProjectsViewModel() }
) {
    LaunchedEffect(SessionManager.activeSession) {
        viewModel.getProjects()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(Res.string.projects_title)) },
                actions = {
                    TeamSwitcher(
                        onTeamSwitch = onTeamSwitch,
                        onCreateTeamClick = onCreateTeamClick
                    )
                }
            )
        },
        floatingActionButton = {
            if (viewModel.uiState.showCreateButton) {
                NewProjectButton(onClick = onNewProjectClick)
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
                                    .padding(bottom = 8.dp)
                                    .animateItem(),
                                project = project,
                                showRenameOption = viewModel.uiState.showRenameOption,
                                showDeleteOption = viewModel.uiState.showDeleteOption,
                                onClick = { onProjectClick(project) },
                                onRenameClick = { viewModel.askToRename(project) },
                                onDeleteClick = { viewModel.askToDelete(project) },
                                onGenerateReportClick = { viewModel.generateReport(project) }
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

            if (viewModel.uiState.error) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .consumeWindowInsets(paddingValues)
                        .padding(paddingValues)
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = stringResource(Res.string.projects_error_message),
                        style = MaterialTheme.typography.bodyLarge,
                        textAlign = TextAlign.Center
                    )

                    TextButton(
                        modifier = Modifier.padding(top = 4.dp),
                        onClick = { viewModel.getProjects() }
                    ) {
                        Icon(Icons.Outlined.Refresh, null)
                        Spacer(Modifier.width(8.dp))
                        Text(stringResource(Res.string.try_again_label))
                    }
                }
            }
        }
    }

    viewModel.uiState.projectToDelete?.let { project ->
        DeleteProjectDialog(
            projectName = project.name,
            onDismissRequest = { viewModel.dismissDeletion() },
            onConfirm = { viewModel.delete(project) }
        )
    }

    viewModel.uiState.projectToRename?.let { project ->
        RenameProjectDialog(
            projectName = project.name,
            onDismissRequest = { viewModel.dismissRenaming() },
            onConfirm = { newName -> viewModel.rename(project, newName) }
        )
    }

    viewModel.uiState.projectToGenerateReport?.let { project ->
        ProjectReportGenerator { pages ->
            onShowReportRequest(pages)
            viewModel.onReportOpened()
        }
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
    showRenameOption: Boolean,
    showDeleteOption: Boolean,
    onClick: () -> Unit,
    onRenameClick: () -> Unit,
    onDeleteClick: () -> Unit,
    onGenerateReportClick: () -> Unit,
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
                var expandOptions by remember { mutableStateOf(false) }

                IconButton(onClick = { expandOptions = true }) {
                    Icon(Icons.Outlined.MoreVert, null)
                }

                ProjectOptionsDropdown(
                    expanded = expandOptions,
                    onDismissRequest = { expandOptions = false },
                    showRenameOption = showRenameOption,
                    showDeleteOption = showDeleteOption,
                    onRenameClick = onRenameClick,
                    onDeleteClick = onDeleteClick,
                    onGenerateReportClick = onGenerateReportClick
                )
            }
        }
    }
}

@Composable
fun ProjectOptionsDropdown(
    expanded: Boolean,
    onDismissRequest: () -> Unit,
    showRenameOption: Boolean,
    showDeleteOption: Boolean,
    onRenameClick: () -> Unit,
    onDeleteClick: () -> Unit,
    onGenerateReportClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    DropdownMenu(
        modifier = modifier,
        expanded = expanded,
        onDismissRequest = onDismissRequest
    ) {
        DropdownMenuItem(
            text = { Text(stringResource(Res.string.generate_report_label)) },
            leadingIcon = { Icon(Icons.Outlined.Info, null) },
            onClick = {
                onGenerateReportClick()
                onDismissRequest()
            }
        )

        if (showRenameOption) {
            DropdownMenuItem(
                text = { Text(stringResource(Res.string.rename_label)) },
                leadingIcon = { Icon(Icons.Outlined.Edit, null) },
                onClick = {
                    onRenameClick()
                    onDismissRequest()
                }
            )
        }

        if (showDeleteOption) {
            DropdownMenuItem(
                text = { Text(stringResource(Res.string.delete_label)) },
                leadingIcon = { Icon(Icons.Outlined.Delete, null) },
                onClick = {
                    onDeleteClick()
                    onDismissRequest()
                }
            )
        }
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
    ConfirmationDialog(
        title = stringResource(Res.string.delete_project_title),
        message = stringResource(Res.string.delete_project_message, projectName),
        onDismissRequest = onDismissRequest,
        onConfirm = onConfirm
    )
}