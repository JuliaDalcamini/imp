package com.julia.imp.artifact.edit

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.julia.imp.artifact.Artifact
import com.julia.imp.artifact.ArtifactFormFields
import com.julia.imp.common.ui.button.PrimaryButton
import com.julia.imp.common.ui.dialog.ErrorDialog
import com.julia.imp.team.inspector.InspectorPickerDialog
import imp.composeapp.generated.resources.Res
import imp.composeapp.generated.resources.arrow_back_24px
import imp.composeapp.generated.resources.edit_artifact_title
import imp.composeapp.generated.resources.load_error_message
import imp.composeapp.generated.resources.load_error_title
import imp.composeapp.generated.resources.save_artifact_error_message
import imp.composeapp.generated.resources.save_artifact_error_title
import imp.composeapp.generated.resources.save_label
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditArtifactScreen(
    artifact: Artifact,
    onBackClick: () -> Unit,
    onArtifactSaved: () -> Unit,
    viewModel: EditArtifactViewModel = viewModel { EditArtifactViewModel() }
) {
    LaunchedEffect(viewModel.uiState.saved) {
        if (viewModel.uiState.saved) onArtifactSaved()
    }

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
                title = { Text(stringResource(Res.string.edit_artifact_title)) }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .consumeWindowInsets(paddingValues)
                .consumeWindowInsets(PaddingValues(24.dp))
                .padding(paddingValues)
                .padding(24.dp)
        ) {
            ArtifactFormFields(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .verticalScroll(rememberScrollState()),
                name = viewModel.uiState.name,
                onNameChange = { viewModel.setName(it) },
                type = viewModel.uiState.type,
                onTypeChange = { viewModel.setType(it) },
                priority = viewModel.uiState.priority,
                onPriorityChange = { viewModel.setPriority(it) },
                inspectors = viewModel.uiState.inspectors,
                onInspectorsChange = { viewModel.setInspectors(it) },
                onAddInspectorClick = { viewModel.openInspectorPicker() },
                availableTypes = viewModel.uiState.availableTypes,
                enabled = !viewModel.uiState.saving
            )

            PrimaryButton(
                modifier = Modifier.fillMaxWidth(),
                label = stringResource(Res.string.save_label),
                onClick = { viewModel.createArtifact() },
                enabled = !viewModel.uiState.saving && viewModel.uiState.canSave,
                loading = viewModel.uiState.saving
            )

            if (viewModel.uiState.showInspectorPicker) {
                InspectorPickerDialog(
                    availableInspectors = viewModel.uiState.availableInspectors,
                    onInspectorSelected = { viewModel.setInspectors(viewModel.uiState.inspectors + it) },
                    onDismissRequest = { viewModel.dismissInspectorPicker() }
                )
            }

            if (viewModel.uiState.saveError) {
                ErrorDialog(
                    title = stringResource(Res.string.save_artifact_error_title),
                    message = stringResource(Res.string.save_artifact_error_message),
                    onDismissRequest = { viewModel.dismissSaveError() }
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
        }
    }
}