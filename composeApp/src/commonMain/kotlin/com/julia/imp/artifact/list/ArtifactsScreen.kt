package com.julia.imp.artifact.list

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.viewmodel.compose.viewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArtifactsScreen(
    projectId: String,
    onBackClick: () -> Unit,
    viewModel: ArtifactsViewModel = viewModel { ArtifactsViewModel() }
) {
    LaunchedEffect(Unit) {
        viewModel.getArtifacts(projectId)
    }

    Column {
        TopAppBar(
            title = { Text("Artefatos $projectId") },
            navigationIcon = {
                IconButton(onClick = onBackClick) {
                    Icon(Icons.AutoMirrored.Outlined.ArrowBack, null)
                }
            }
        )

        if (viewModel.uiState.isLoading) {
            CircularProgressIndicator()
        }

        viewModel.uiState.artifacts?.let { artifacts ->
            ArtifactsList(artifacts)
        }
    }

}

@Composable
fun ArtifactsList(artifacts: List<ArtifactListEntry>) {
    LazyColumn {
        items(artifacts) { artifact ->
            Text(
                text = artifact.name,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}
