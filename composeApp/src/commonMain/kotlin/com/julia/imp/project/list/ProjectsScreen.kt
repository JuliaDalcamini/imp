package com.julia.imp.project.list

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
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.julia.imp.common.ui.theme.ImpTheme
import com.julia.imp.common.ui.tooling.Preview
import com.julia.imp.project.Project
import com.julia.imp.team.Team
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
import imp.composeapp.generated.resources.project_options_label
import imp.composeapp.generated.resources.projects_error_message
import imp.composeapp.generated.resources.projects_title
import imp.composeapp.generated.resources.rename_label
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProjectsScreen(
    currentTeam: Team,
    onTeamChanged: (Team) -> Unit,
    viewModel: ProjectsViewModel = viewModel { ProjectsViewModel() }
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(Res.string.projects_title)) },
                actions = {
                    TeamSwitcher(
                        currentTeam = currentTeam,
                        onTeamChanged = onTeamChanged
                    )
                }
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                text = { Text(stringResource(Res.string.new_project_label)) },
                icon = { Icon(Icons.Default.Add, null) },
                onClick = {}
            )
        }
    ) { paddingValues ->
        Box(Modifier.fillMaxSize()) {
            val uiState by viewModel.uiState.collectAsState()

            LaunchedEffect(currentTeam) {
                viewModel.getProjects(currentTeam.id)
            }

            if (uiState.isLoading) {
                CircularProgressIndicator(Modifier.align(Alignment.Center).padding(paddingValues))
            }

            uiState.projects?.let { projects ->
                if (projects.isNotEmpty()) {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize().consumeWindowInsets(paddingValues),
                        contentPadding = paddingValues
                    ) {
                        items(projects) { project ->
                            ProjectListItem(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp)
                                    .padding(bottom = 8.dp),
                                project = project,
                                onClick = {},
                                onRenameClick = {},
                                onDeleteClick = { viewModel.askToDeleteProject(project) }
                            )
                        }

                        item {
                            Spacer(Modifier.height(48.dp))
                        }
                    }
                } else {
                    Text(
                        modifier = Modifier.padding(24.dp).align(Alignment.Center),
                        text = stringResource(Res.string.no_projects_message)
                    )
                }
            }

            uiState.error?.let {
                Text(
                    modifier = Modifier
                        .padding(paddingValues)
                        .padding(24.dp)
                        .align(Alignment.Center),
                    text = stringResource(Res.string.projects_error_message)
                )
            }

            uiState.projectToDelete?.let { project ->
                DeleteProjectDialog(
                    project = project,
                    onDismissRequest = { viewModel.dismissProjectDeletion() },
                    onConfirm = { viewModel.deleteProject(project, currentTeam.id) }
                )
            }
        }
    }
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
                    text = stringResource(Res.string.created_by_format, project.creator.name),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Box {
                var showOptions by remember { mutableStateOf(false) }

                IconButton(onClick = { showOptions = true }) {
                    Icon(Icons.Outlined.MoreVert, stringResource(Res.string.project_options_label))
                }

                DropdownMenu(
                    expanded = showOptions,
                    onDismissRequest = { showOptions = false }
                ) {
                    DropdownMenuItem(
                        text = { Text(stringResource(Res.string.rename_label)) },
                        onClick = {
                            onRenameClick()
                            showOptions = false
                        }
                    )

                    DropdownMenuItem(
                        text = { Text(stringResource(Res.string.delete_label)) },
                        onClick = {
                            onDeleteClick()
                            showOptions = false
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun DeleteProjectDialog(
    project: Project,
    onDismissRequest: () -> Unit,
    onConfirm: () -> Unit
) {
    AlertDialog(
        icon = { Icon(Icons.Outlined.Warning, null) },
        title = { Text(text = stringResource(Res.string.delete_project_title)) },
        text = { Text(text = stringResource(Res.string.delete_project_message)) },
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

@Preview
@Composable
fun ProjectsScreenPreview() {
    ImpTheme {
        ProjectsScreen(
            currentTeam = Team(id = "", name = "Time da Julia"),
            onTeamChanged = {}
        )
    }
}