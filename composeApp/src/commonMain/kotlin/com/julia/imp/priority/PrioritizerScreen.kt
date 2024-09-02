package com.julia.imp.priority

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.julia.imp.artifact.Artifact
import com.julia.imp.artifact.details.ArchivedArtifactAlert
import com.julia.imp.artifact.details.ArtifactDetailsViewModel
import com.julia.imp.common.ui.button.PrimaryButton
import com.julia.imp.common.ui.dialog.ErrorDialog
import com.julia.imp.common.ui.title.CompoundTitle
import imp.composeapp.generated.resources.Res
import imp.composeapp.generated.resources.action_error_message
import imp.composeapp.generated.resources.action_error_title
import imp.composeapp.generated.resources.arrow_back_24px
import imp.composeapp.generated.resources.artifact_details_title
import imp.composeapp.generated.resources.artifact_name_label
import imp.composeapp.generated.resources.artifact_type_label
import imp.composeapp.generated.resources.create_artifact_label
import imp.composeapp.generated.resources.edit_24px
import imp.composeapp.generated.resources.load_error_message
import imp.composeapp.generated.resources.load_error_title
import imp.composeapp.generated.resources.prioritize_artifact_error_message
import imp.composeapp.generated.resources.prioritize_artifact_error_title
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrioritizerScreen(
    prioritizer: Prioritizer,
    artifact: Artifact,
    onBackClick: () -> Unit,
    onEditClick: () -> Unit,
    detailsViewModel: ArtifactDetailsViewModel = androidx.lifecycle.viewmodel.compose.viewModel { ArtifactDetailsViewModel() },
    viewModel: PrioritizerViewModel
) {
    LaunchedEffect(artifact) {
        detailsViewModel.initialize(artifact)
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
                title = {
                    CompoundTitle(
                        title = stringResource(Res.string.artifact_details_title),
                        subtitle = artifact.name
                    )
                },
                actions = {
                    if (!artifact.archived) {
                        IconButton(onClick = onEditClick) {
                            Icon(vectorResource(Res.drawable.edit_24px), null)
                        }
                    }
                }
            )
        },
    ) { paddingValues ->
        if (detailsViewModel.uiState.loading) {
            Placeholder(
                modifier = Modifier
                    .fillMaxSize()
                    .consumeWindowInsets(paddingValues)
                    .consumeWindowInsets(PaddingValues(24.dp))
                    .padding(paddingValues)
                    .padding(24.dp)
            )
        } else {
            ArtifactDetails(
                modifier = Modifier
                    .fillMaxSize()
                    .consumeWindowInsets(paddingValues)
                    .consumeWindowInsets(PaddingValues(vertical = 24.dp))
                    .padding(paddingValues)
                    .padding(vertical = 24.dp)
                    .verticalScroll(rememberScrollState()),
                artifact = artifact
            )

            PrioritizerFormFields(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                priority = viewModel.uiState.priority,
                onPriorityChange = { viewModel.setPriority(it) },
                enabled = !detailsViewModel.uiState.loading
            )

            PrimaryButton(
                modifier = Modifier.fillMaxWidth(),
                label = stringResource(Res.string.create_artifact_label),
                onClick = { viewModel.prioritizeArtifact() },
                enabled = !viewModel.uiState.loading && viewModel.uiState.canPrioritize,
                loading = viewModel.uiState.loading
            )
        }
    }

    if (viewModel.uiState.prioritizeError) {
        ErrorDialog(
            title = stringResource(Res.string.prioritize_artifact_error_title),
            message = stringResource(Res.string.prioritize_artifact_error_message),
            onDismissRequest = { viewModel.dismissPrioritizeError() }
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

    if (viewModel.uiState.actionError) {
        ErrorDialog(
            title = stringResource(Res.string.action_error_title),
            message = stringResource(Res.string.action_error_message),
            onDismissRequest = { viewModel.dismissActionError() }
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
fun ArtifactDetails(
    artifact: Artifact,
    modifier: Modifier = Modifier
) {
    Column(modifier) {
        if (artifact.archived) {
            ArchivedArtifactAlert(
                modifier = Modifier
                    .padding(horizontal = 24.dp)
                    .padding(bottom = 24.dp)
            )
        }

        Text(
            modifier = Modifier
                .padding(horizontal = 24.dp)
                .padding(bottom = 4.dp),
            style = MaterialTheme.typography.labelMedium,
            text = stringResource(Res.string.artifact_name_label)
        )

        Text(
            modifier = Modifier.padding(horizontal = 24.dp),
            style = MaterialTheme.typography.bodyMedium,
            text = artifact.name
        )

        Text(
            modifier = Modifier
                .padding(horizontal = 24.dp)
                .padding(top = 24.dp, bottom = 4.dp),
            style = MaterialTheme.typography.labelMedium,
            text = stringResource(Res.string.artifact_type_label)
        )

        Text(
            modifier = Modifier.padding(horizontal = 24.dp),
            style = MaterialTheme.typography.bodyMedium,
            text = artifact.type.name
        )
    }
}