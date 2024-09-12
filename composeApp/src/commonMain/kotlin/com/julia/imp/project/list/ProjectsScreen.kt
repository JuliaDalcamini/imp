package com.julia.imp.project.list

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import com.julia.imp.common.ui.topbar.TopBar
import com.julia.imp.project.Project
import com.julia.imp.project.ProjectFilter
import com.julia.imp.report.ProjectReportGenerator
import com.julia.imp.team.switcher.TeamSwitcher
import imp.composeapp.generated.resources.Res
import imp.composeapp.generated.resources.action_error_message
import imp.composeapp.generated.resources.action_error_title
import imp.composeapp.generated.resources.add_24px
import imp.composeapp.generated.resources.created_by_format
import imp.composeapp.generated.resources.delete_24px
import imp.composeapp.generated.resources.delete_label
import imp.composeapp.generated.resources.delete_project_message
import imp.composeapp.generated.resources.delete_project_title
import imp.composeapp.generated.resources.description_24px
import imp.composeapp.generated.resources.filter_active
import imp.composeapp.generated.resources.filter_all
import imp.composeapp.generated.resources.filter_finished
import imp.composeapp.generated.resources.generate_report_label
import imp.composeapp.generated.resources.manage_label
import imp.composeapp.generated.resources.more_vert_24px
import imp.composeapp.generated.resources.new_project_label
import imp.composeapp.generated.resources.no_projects_message
import imp.composeapp.generated.resources.projects_error_message
import imp.composeapp.generated.resources.projects_title
import imp.composeapp.generated.resources.query_stats_24px
import imp.composeapp.generated.resources.refresh_24px
import imp.composeapp.generated.resources.settings_24px
import imp.composeapp.generated.resources.try_again_label
import imp.composeapp.generated.resources.view_stats_label
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource

@Composable
fun ProjectsScreen(
    onProjectClick: (Project) -> Unit,
    onNewProjectClick: () -> Unit,
    onViewProjectStatsClick: (Project) -> Unit,
    onManageProjectClick: (Project) -> Unit,
    onTeamSwitch: (UserSession) -> Unit,
    onManageTeamClick: () -> Unit,
    onCreateTeamClick: () -> Unit,
    onShowReportRequest: (List<ImageBitmap>) -> Unit,
    viewModel: ProjectsViewModel = viewModel { ProjectsViewModel() }
) {
    LaunchedEffect(SessionManager.activeSession) {
        viewModel.getProjects(ProjectFilter.Active)
    }

    Scaffold(
        topBar = {
            TopBar(
                title = stringResource(Res.string.projects_title),
                actions = {
                    TeamSwitcher(
                        onTeamSwitch = onTeamSwitch,
                        onManageTeamClick = onManageTeamClick,
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

            ProjectList(
                modifier = Modifier
                    .fillMaxSize()
                    .consumeWindowInsets(paddingValues),
                entries = viewModel.uiState.projects,
                onProjectClick = onProjectClick,
                onViewProjectStatsClick = onViewProjectStatsClick,
                onManageProjectClick = onManageProjectClick,
                onDeleteProjectClick = { viewModel.askToDelete(it) },
                onGenerateProjectClick = { viewModel.generateReport(it) },
                selectedFilter = viewModel.uiState.filter,
                onFilterChange = { viewModel.setFilter(it) },
                showDeleteOption = viewModel.uiState.showDeleteOption,
                showManageOption = viewModel.uiState.showManageOption,
                contentPadding = paddingValues
            )

            if (viewModel.uiState.projects?.isEmpty() == true) {
                Text(
                    modifier = Modifier.padding(24.dp).align(Alignment.Center),
                    text = stringResource(Res.string.no_projects_message)
                )
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
                        Icon(vectorResource(Res.drawable.refresh_24px), null)
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

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ProjectList(
    entries: List<Project>?,
    onProjectClick: (Project) -> Unit,
    onViewProjectStatsClick: (Project) -> Unit,
    onManageProjectClick: (Project) -> Unit,
    onDeleteProjectClick: (Project) -> Unit,
    onGenerateProjectClick: (Project) -> Unit,
    selectedFilter: ProjectFilter,
    onFilterChange: (ProjectFilter) -> Unit,
    showDeleteOption: Boolean,
    showManageOption: Boolean,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues()
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = contentPadding
    ) {
        stickyHeader("filters") {
            ProjectListFilters(
                modifier = Modifier.fillMaxWidth(),
                selectedFilter = selectedFilter,
                onFilterChange = onFilterChange
            )
        }

        entries?.let {
            items(entries) { project ->
                ProjectListItem(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .padding(bottom = 8.dp)
                        .animateItem(),
                    project = project,
                    showDeleteOption = showDeleteOption,
                    showManageOption = showManageOption,
                    onClick = { onProjectClick(project) },
                    onViewStatsClick = { onViewProjectStatsClick(project) },
                    onManageProjectClick = { onManageProjectClick(project) },
                    onDeleteClick = { onDeleteProjectClick(project) },
                    onGenerateReportClick = { onGenerateProjectClick(project) }
                )
            }
        }

        item {
            Spacer(Modifier.height(56.dp))
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
        icon = { Icon(vectorResource(Res.drawable.add_24px), null) },
        onClick = onClick
    )
}

@Composable
private fun ProjectListFilters(
    modifier: Modifier,
    selectedFilter: ProjectFilter,
    onFilterChange: (ProjectFilter) -> Unit
) {
    LazyRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(horizontal = 16.dp)
    ) {
        items(ProjectFilter.entries) {
            FilterChip(
                selected = it == selectedFilter,
                onClick = { onFilterChange(it) },
                label = { Text(getFilterText(it)) }
            )
        }
    }
}

@Composable
private fun getFilterText(filter: ProjectFilter): String =
    when (filter) {
        ProjectFilter.Active -> stringResource(Res.string.filter_active)
        ProjectFilter.Finished -> stringResource(Res.string.filter_finished)
        ProjectFilter.All -> stringResource(Res.string.filter_all)
    }

@Composable
private fun ProjectListItem(
    project: Project,
    showDeleteOption: Boolean,
    showManageOption: Boolean,
    onClick: () -> Unit,
    onViewStatsClick: () -> Unit,
    onManageProjectClick: () -> Unit,
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
                    Icon(vectorResource(Res.drawable.more_vert_24px), null)
                }

                ProjectOptionsDropdown(
                    expanded = expandOptions,
                    onDismissRequest = { expandOptions = false },
                    showDeleteOption = showDeleteOption,
                    showManageOption = showManageOption,
                    onViewStatsClick = onViewStatsClick,
                    onManageProjectClick = onManageProjectClick,
                    onDeleteClick = onDeleteClick,
                    onGenerateReportClick = onGenerateReportClick
                )
            }
        }
    }
}

@Composable
private fun ProjectOptionsDropdown(
    expanded: Boolean,
    onDismissRequest: () -> Unit,
    showDeleteOption: Boolean,
    showManageOption: Boolean,
    onViewStatsClick: () -> Unit,
    onManageProjectClick: () -> Unit,
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
            text = { Text(stringResource(Res.string.view_stats_label)) },
            leadingIcon = { Icon(vectorResource(Res.drawable.query_stats_24px), null) },
            onClick = {
                onViewStatsClick()
                onDismissRequest()
            }
        )

        DropdownMenuItem(
            text = { Text(stringResource(Res.string.generate_report_label)) },
            leadingIcon = { Icon(vectorResource(Res.drawable.description_24px), null) },
            onClick = {
                onGenerateReportClick()
                onDismissRequest()
            }
        )

        if (showManageOption) {
            DropdownMenuItem(
                text = { Text(stringResource(Res.string.manage_label)) },
                leadingIcon = { Icon(vectorResource(Res.drawable.settings_24px), null) },
                onClick = {
                    onManageProjectClick()
                    onDismissRequest()
                }
            )
        }

        if (showDeleteOption) {
            DropdownMenuItem(
                text = { Text(stringResource(Res.string.delete_label)) },
                leadingIcon = { Icon(vectorResource(Res.drawable.delete_24px), null) },
                onClick = {
                    onDeleteClick()
                    onDismissRequest()
                }
            )
        }
    }
}

@Composable
private fun DeleteProjectDialog(
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