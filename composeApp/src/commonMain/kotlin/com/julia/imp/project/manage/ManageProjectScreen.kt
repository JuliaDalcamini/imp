package com.julia.imp.project.manage

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ListItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.julia.imp.common.datetime.DateFormats
import com.julia.imp.common.ui.dialog.DatePickerDialog
import com.julia.imp.common.ui.dialog.SelectionDialog
import com.julia.imp.common.ui.dialog.TextInputDialog
import com.julia.imp.common.ui.topbar.TopBar
import com.julia.imp.project.Project
import imp.composeapp.generated.resources.Res
import imp.composeapp.generated.resources.current_min_inspectors_format
import imp.composeapp.generated.resources.current_name_format
import imp.composeapp.generated.resources.inspectors_number_label
import imp.composeapp.generated.resources.inspectors_number_select
import imp.composeapp.generated.resources.manage_project_title
import imp.composeapp.generated.resources.rename_label
import imp.composeapp.generated.resources.rename_project_title
import imp.composeapp.generated.resources.target_date_format
import imp.composeapp.generated.resources.target_date_label
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format
import kotlinx.datetime.todayIn
import org.jetbrains.compose.resources.stringResource

@Composable
fun ManageProjectScreen(
    project: Project,
    onBackClick: () -> Unit,
    onProjectUpdate: (Project) -> Unit,
    viewModel: ManageProjectViewModel = viewModel { ManageProjectViewModel() }
) {
    LaunchedEffect(viewModel.uiState.updatedProject) {
        viewModel.uiState.updatedProject?.let {
            onProjectUpdate(it)
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
                    .clickable { viewModel.showChangeTargetDateDialog() },
                headlineContent = { Text(stringResource(Res.string.target_date_label)) },
                supportingContent = {
                    Text(
                        stringResource(
                            Res.string.target_date_format,
                            project.targetDate.format(DateFormats.DEFAULT)
                        )
                    )
                }
            )
        }
    }

    if (viewModel.uiState.showRenameDialog) {
        RenameProjectDialog(
            projectName = project.name,
            onDismissRequest = { viewModel.dismissRenameDialog() },
            onConfirm = { newName -> viewModel.rename(project, newName) }
        )
    }

    if (viewModel.uiState.showChangeMinInspectorsDialog) {
        ChangeMinInspectorsDialog(
            minInspectors = project.minInspectors,
            onDismissRequest = { viewModel.dismissChangeMinInspectorsDialog() },
            onConfirm = { newMinInspectors ->
                viewModel.changeMinInspectors(project, newMinInspectors)
            }
        )
    }

    if (viewModel.uiState.showChangeTargetDateDialog) {
        DatePickerDialog(
            initialDate = project.targetDate,
            minDate = Clock.System.todayIn(TimeZone.currentSystemDefault()),
            onDismissRequest = { viewModel.dismissChangeTargetDateDialog() },
            onConfirm = { newTargetDate ->
                viewModel.changeTargetDate(project, newTargetDate)
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
    SelectionDialog(
        title = stringResource(Res.string.inspectors_number_select),
        options = (2..5).toList(),
        onDismissRequest = onDismissRequest,
        initialSelection = minInspectors,
        onConfirm = onConfirm
    )
}