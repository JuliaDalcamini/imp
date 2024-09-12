package com.julia.imp.project.manage

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.julia.imp.common.datetime.DateFormats
import com.julia.imp.common.ui.dialog.ConfirmationDialog
import com.julia.imp.common.ui.dialog.DatePickerDialog
import com.julia.imp.common.ui.dialog.ErrorDialog
import com.julia.imp.common.ui.dialog.SelectionDialog
import com.julia.imp.common.ui.dialog.TextInputDialog
import com.julia.imp.common.ui.topbar.TopBar
import imp.composeapp.generated.resources.Res
import imp.composeapp.generated.resources.current_min_inspectors_format
import imp.composeapp.generated.resources.current_name_format
import imp.composeapp.generated.resources.finish_project_description
import imp.composeapp.generated.resources.finish_project_label
import imp.composeapp.generated.resources.finish_project_message
import imp.composeapp.generated.resources.finish_project_title
import imp.composeapp.generated.resources.inspectors_number_label
import imp.composeapp.generated.resources.inspectors_number_select
import imp.composeapp.generated.resources.load_error_message
import imp.composeapp.generated.resources.load_error_title
import imp.composeapp.generated.resources.manage_project_title
import imp.composeapp.generated.resources.rename_label
import imp.composeapp.generated.resources.rename_project_title
import imp.composeapp.generated.resources.start_date_label
import imp.composeapp.generated.resources.string_format
import imp.composeapp.generated.resources.target_date_label
import imp.composeapp.generated.resources.update_number_inspectors_message
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format
import kotlinx.datetime.todayIn
import org.jetbrains.compose.resources.stringResource

@Composable
fun ManageProjectScreen(
    projectId: String,
    onBackClick: () -> Unit,
    onProjectFinished: () -> Unit,
    viewModel: ManageProjectViewModel = viewModel { ManageProjectViewModel() }
) {
    val project = viewModel.uiState.project

    LaunchedEffect(projectId) {
        viewModel.initialize(projectId)
    }

    LaunchedEffect(viewModel.uiState.projectFinished) {
        if (viewModel.uiState.projectFinished) {
            onProjectFinished()
        }
    }

    Scaffold(
        topBar = {
            TopBar(
                title = stringResource(Res.string.manage_project_title),
                onBackClick = onBackClick
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
        } else if (project != null) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .consumeWindowInsets(paddingValues)
                    .padding(paddingValues)
            ) {
                ListItem(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { viewModel.showRenameDialog() },
                    headlineContent = { Text(stringResource(Res.string.rename_label)) },
                    supportingContent = {
                        Text(
                            stringResource(
                                Res.string.current_name_format,
                                project.name
                            )
                        )
                    }
                )

                ListItem(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { viewModel.showChangeMinInspectorsDialog() },
                    headlineContent = { Text(stringResource(Res.string.inspectors_number_label)) },
                    supportingContent = {
                        Text(
                            stringResource(
                                Res.string.current_min_inspectors_format,
                                project.minInspectors
                            )
                        )
                    }
                )

                ListItem(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { viewModel.showChangeStartDateDialog() },
                    headlineContent = { Text(stringResource(Res.string.start_date_label)) },
                    supportingContent = {
                        Text(
                            stringResource(
                                Res.string.string_format,
                                project.startDate.format(DateFormats.DEFAULT)
                            )
                        )
                    }
                )

                ListItem(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { viewModel.showChangeTargetDateDialog() },
                    headlineContent = { Text(stringResource(Res.string.target_date_label)) },
                    supportingContent = {
                        Text(
                            stringResource(
                                Res.string.string_format,
                                project.targetDate.format(DateFormats.DEFAULT)
                            )
                        )
                    }
                )

                ListItem(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { viewModel.showFinishDialog() },
                    headlineContent = { Text(stringResource(Res.string.finish_project_label)) },
                    supportingContent = { Text(stringResource(Res.string.finish_project_description)) }
                )
            }
        }
    }

    if (project != null && viewModel.uiState.showRenameDialog) {
        RenameProjectDialog(
            projectName = project.name,
            onDismissRequest = { viewModel.dismissRenameDialog() },
            onConfirm = { newName -> viewModel.rename(project, newName) }
        )
    }

    if (project != null && viewModel.uiState.showChangeMinInspectorsDialog) {
        ChangeMinInspectorsDialog(
            minInspectors = project.minInspectors,
            onDismissRequest = { viewModel.dismissChangeMinInspectorsDialog() },
            onConfirm = { newMinInspectors ->
                viewModel.changeMinInspectors(project, newMinInspectors)
            }
        )
    }

    if (project != null && viewModel.uiState.showChangeStartDateDialog) {
        DatePickerDialog(
            initialDate = project.startDate,
            minDate = Clock.System.todayIn(TimeZone.currentSystemDefault()),
            onDismissRequest = { viewModel.dismissChangeStartDateDialog() },
            onConfirm = { newStartDate ->
                viewModel.changeStartDate(project, newStartDate)
            }
        )
    }

    if (project != null && viewModel.uiState.showChangeTargetDateDialog) {
        DatePickerDialog(
            initialDate = project.targetDate,
            minDate = Clock.System.todayIn(TimeZone.currentSystemDefault()),
            onDismissRequest = { viewModel.dismissChangeTargetDateDialog() },
            onConfirm = { newTargetDate ->
                viewModel.changeTargetDate(project, newTargetDate)
            }
        )
    }

    if (project != null && viewModel.uiState.showFinishDialog) {
        ConfirmationDialog(
            title = stringResource(Res.string.finish_project_title),
            message = stringResource(Res.string.finish_project_message, project.name),
            onDismissRequest = { viewModel.dismissFinishDialog() },
            onConfirm = { viewModel.finishProject(project) }
        )
    }

    if (project != null && viewModel.uiState.loadError) {
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
fun RenameProjectDialog(
    projectName: String,
    onDismissRequest: () -> Unit,
    onConfirm: (String) -> Unit
) {
    TextInputDialog(
        title = stringResource(Res.string.rename_project_title),
        initialValue = projectName,
        onDismissRequest = onDismissRequest,
        onConfirm = onConfirm
    )
}

@Composable
fun ChangeMinInspectorsDialog(
    minInspectors: Int,
    onDismissRequest: () -> Unit,
    onConfirm: (Int) -> Unit
) {
    Text(
        text = stringResource(Res.string.update_number_inspectors_message),
        style = MaterialTheme.typography.labelMedium
    )

    SelectionDialog(
        title = stringResource(Res.string.inspectors_number_select),
        options = (2..5).toList(),
        onDismissRequest = onDismissRequest,
        initialSelection = minInspectors,
        onConfirm = onConfirm,
        message = stringResource(Res.string.update_number_inspectors_message)
    )
}