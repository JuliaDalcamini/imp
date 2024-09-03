package com.julia.imp.artifact.prioritize

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.julia.imp.artifact.Artifact
import com.julia.imp.artifact.details.ArchivedArtifactAlert
import com.julia.imp.common.text.toLink
import com.julia.imp.common.ui.button.PrimaryButton
import com.julia.imp.common.ui.dialog.ErrorDialog
import com.julia.imp.common.ui.title.CompoundTitle
import com.julia.imp.priority.Prioritizer
import com.julia.imp.priority.Priority
import imp.composeapp.generated.resources.Res
import imp.composeapp.generated.resources.action_error_message
import imp.composeapp.generated.resources.action_error_title
import imp.composeapp.generated.resources.arrow_back_24px
import imp.composeapp.generated.resources.artifact_external_link_label
import imp.composeapp.generated.resources.artifact_name_label
import imp.composeapp.generated.resources.artifact_type_label
import imp.composeapp.generated.resources.conclude_label
import imp.composeapp.generated.resources.load_error_message
import imp.composeapp.generated.resources.load_error_title
import imp.composeapp.generated.resources.prioritize_artifact_error_message
import imp.composeapp.generated.resources.prioritize_artifact_error_title
import imp.composeapp.generated.resources.prioritize_artifact_title
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrioritizeArtifactScreen(
    artifact: Artifact,
    prioritizer: Prioritizer,
    onBackClick: () -> Unit,
    onArtifactSaved: (Artifact) -> Unit,
    viewModel: PrioritizeArtifactViewModel = viewModel { PrioritizeArtifactViewModel() }
) {
    LaunchedEffect(viewModel.uiState.savedArtifact) {
        viewModel.uiState.savedArtifact?.let { onArtifactSaved(it) }
    }

    LaunchedEffect(artifact) {
        viewModel.initialize(artifact, prioritizer)
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
                        title = stringResource(Res.string.prioritize_artifact_title),
                        subtitle = artifact.name
                    )
                },
                actions = { }
            )
        },
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
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .consumeWindowInsets(paddingValues)
                    .consumeWindowInsets(PaddingValues(vertical = 24.dp))
                    .padding(paddingValues)
                    .padding(vertical = 24.dp)
            ) {
                ScreenContents(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .verticalScroll(rememberScrollState()),
                    artifact = artifact,
                    priority = viewModel.uiState.priority,
                    onPriorityChange = { viewModel.setPriority(it) },
                    enabled = !viewModel.uiState.saving
                )

                PrimaryButton(
                    modifier = Modifier.fillMaxWidth(),
                    label = stringResource(Res.string.conclude_label),
                    onClick = { viewModel.prioritizeArtifact() },
                    enabled = !viewModel.uiState.saving,
                    loading = viewModel.uiState.saving
                )
            }
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
private fun ScreenContents(
    artifact: Artifact,
    priority: Priority?,
    onPriorityChange: (Priority) -> Unit,
    enabled: Boolean,
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

        Text(
            modifier = Modifier
                .padding(horizontal = 24.dp)
                .padding(top = 24.dp, bottom = 4.dp),
            style = MaterialTheme.typography.labelMedium,
            text = stringResource(Res.string.artifact_external_link_label)
        )

        ExternalLink(
            modifier = Modifier.padding(horizontal = 24.dp),
            url = artifact.externalLink
        )

        PriorityFormFields(
            modifier = Modifier
                .padding(horizontal = 24.dp)
                .padding(top = 24.dp)
                .fillMaxWidth(),
            priority = priority,
            onPriorityChange = onPriorityChange,
            enabled = enabled
        )

        Spacer(Modifier.height(24.dp))
    }
}

@Composable
private fun ExternalLink(
    url: String,
    modifier: Modifier = Modifier
) {
    val uriHandler = LocalUriHandler.current

    Text(
        modifier = modifier.clickable { uriHandler.openUri(url) },
        style = MaterialTheme.typography.bodyMedium,
        text = url.toLink()
    )
}