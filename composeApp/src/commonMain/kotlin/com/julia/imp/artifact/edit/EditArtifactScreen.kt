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
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.julia.imp.artifact.Artifact
import com.julia.imp.artifact.ArtifactFormFields
import com.julia.imp.common.ui.button.PrimaryButton
import com.julia.imp.common.ui.dialog.ConfirmationDialog
import com.julia.imp.common.ui.dialog.ErrorDialog
import com.julia.imp.common.ui.topbar.TopBar
import com.julia.imp.user.UserPickerDialog
import imp.composeapp.generated.resources.Res
import imp.composeapp.generated.resources.confirm_update_artifact_message
import imp.composeapp.generated.resources.confirm_update_artifact_title
import imp.composeapp.generated.resources.edit_artifact_title
import imp.composeapp.generated.resources.load_error_message
import imp.composeapp.generated.resources.load_error_title
import imp.composeapp.generated.resources.save_artifact_error_message
import imp.composeapp.generated.resources.save_artifact_error_title
import imp.composeapp.generated.resources.save_label
import imp.composeapp.generated.resources.select_inspector_label
import org.jetbrains.compose.resources.stringResource

@Composable
fun EditArtifactScreen(
    artifact: Artifact,
    onBackClick: () -> Unit,
    onArtifactSaved: (Artifact) -> Unit,
    viewModel: EditArtifactViewModel = viewModel { EditArtifactViewModel() }
) {
    LaunchedEffect(viewModel.uiState.savedArtifact) {
        viewModel.uiState.savedArtifact?.let { onArtifactSaved(it) }
    }

    LaunchedEffect(artifact) {
        viewModel.initialize(artifact)
    }

    Scaffold(
        modifier = Modifier.imePadding(),
        topBar = {
            TopBar(
                title = stringResource(Res.string.edit_artifact_title),
                onBackClick = onBackClick
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
                currentVersion = viewModel.uiState.version,
                onVersionChange = { viewModel.setCurrentVersion(it) },
                needsVersionChange = viewModel.uiState.needsVersionChange,
                inspectors = viewModel.uiState.inspectors,
                onAddInspectorClick = { viewModel.openInspectorPicker() },
                onRemoveInspectorClick = { viewModel.removeInspector(it) },
                availableTypes = viewModel.uiState.availableTypes,
                externalLink = viewModel.uiState.externalLink,
                onExternalLinkChange = { viewModel.setExternalLink(it) },
                enabled = !viewModel.uiState.saving
            )

            PrimaryButton(
                modifier = Modifier.fillMaxWidth(),
                label = stringResource(Res.string.save_label),
                onClick = { viewModel.askToSave() },
                enabled = !viewModel.uiState.saving && viewModel.uiState.canSave,
                loading = viewModel.uiState.saving
            )
        }
    }

    if (viewModel.uiState.showInspectorPicker) {
        UserPickerDialog(
            title = stringResource(Res.string.select_inspector_label),
            availableUsers = viewModel.uiState.availableInspectors,
            onUserSelected = { viewModel.addInspector(it) },
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

    if (viewModel.uiState.showSaveDialog) {
        ConfirmSaveDialog(
            onDismissRequest = { viewModel.dismissSaveDialog() },
            onConfirm = { viewModel.updateArtifact() }
        )
    }
}

@Composable
private fun ConfirmSaveDialog(
    onDismissRequest: () -> Unit,
    onConfirm: () -> Unit
) {
    ConfirmationDialog(
        title = stringResource(Res.string.confirm_update_artifact_title),
        message = stringResource(Res.string.confirm_update_artifact_message),
        onDismissRequest = onDismissRequest,
        onConfirm = onConfirm
    )
}