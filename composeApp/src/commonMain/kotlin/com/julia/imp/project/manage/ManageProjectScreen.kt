package com.julia.imp.project.manage

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.julia.imp.common.ui.dialog.TextInputDialog
import com.julia.imp.common.ui.form.DropdownFormField
import com.julia.imp.common.ui.title.Title
import com.julia.imp.project.Project
import imp.composeapp.generated.resources.Res
import imp.composeapp.generated.resources.arrow_back_24px
import imp.composeapp.generated.resources.current_min_inspectors_format
import imp.composeapp.generated.resources.current_name_format
import imp.composeapp.generated.resources.inspectors_number_label
import imp.composeapp.generated.resources.inspectors_number_select
import imp.composeapp.generated.resources.manage_team_title
import imp.composeapp.generated.resources.new_target_date_title
import imp.composeapp.generated.resources.rename_label
import imp.composeapp.generated.resources.rename_project_title
import imp.composeapp.generated.resources.rename_team_title
import imp.composeapp.generated.resources.target_date_format
import imp.composeapp.generated.resources.target_date_label
import imp.composeapp.generated.resources.update_cost_default_title
import kotlinx.datetime.LocalDate
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource

@OptIn(ExperimentalMaterial3Api::class)
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
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(vectorResource(Res.drawable.arrow_back_24px), null)
                    }
                },
                title = { Title(stringResource(Res.string.manage_team_title)) }
            )
        }
    ) { paddingValues ->
        val scrollState = rememberScrollState()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .consumeWindowInsets(paddingValues)
                .padding(paddingValues)
                .verticalScroll(scrollState)
        ) {
            ListItem(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { viewModel.showRenameDialog() },
                headlineContent = { Text(stringResource(Res.string.rename_label)) },
                supportingContent = { Text(stringResource(Res.string.current_name_format, project.name)) }
            )

            ListItem(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { viewModel.showChangeMinInspectorsDialog() },
                headlineContent = { Text(stringResource(Res.string.inspectors_number_label)) },
                supportingContent = { Text(stringResource(Res.string.current_min_inspectors_format, project.minInspectors)) }
            )

            ListItem(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { viewModel.showChangeTargetDateDialog() },
                headlineContent = { Text(stringResource(Res.string.target_date_label)) },
                supportingContent = { Text(stringResource(Res.string.target_date_format, project.targetDate.toString())) }
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
                viewModel.changeMinInspectors(project, newMinInspectors.toInt()) }
        )
    }

    if (viewModel.uiState.showChangeTargetDateDialog) {
        ChangeTargetDateDialog(
            targetDate = project.targetDate,
            onDismissRequest = { viewModel.dismissChangeTargetDateDialog() },
            onConfirm = { newTargetDate ->
                viewModel.changeTargetDate(project, LocalDate.parse(newTargetDate)) }
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
    onConfirm: (String) -> Unit
) {
    TextInputDialog(
        title = stringResource(Res.string.inspectors_number_select),
        initialValue = minInspectors.toString(),
        onDismissRequest = onDismissRequest,
        onConfirm = onConfirm
    )

//    val inspectorOptions = listOf(2, 3, 4, 5)
//
//    DropdownFormField(
//        modifier = Modifier.fillMaxWidth().padding(top = 24.dp),
//        text = minInspectors.toString(),
//        label = stringResource(Res.string.inspectors_number_label),
//        options = inspectorOptions,
//        onOptionSelected = onSetInspectorCount(it),
//        enabled = !viewModel.uiState.loading,
//        optionLabel = { optionNumber -> Text(optionNumber.toString()) },
//        onDismissRequest = onDismissRequest,
//        onConfirm = onConfirm
//    )
}

@Composable
fun ChangeTargetDateDialog(
    targetDate: LocalDate,
    onDismissRequest: () -> Unit,
    onConfirm: (String) -> Unit
) {
    TextInputDialog(
        title = stringResource(Res.string.new_target_date_title),
        initialValue = targetDate.toString(),
        onDismissRequest = onDismissRequest,
        onConfirm = onConfirm
    )
}