package com.julia.imp.artifact.list

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArtifactsScreen(projectId: String) {
    Column {
        TopAppBar(
            title = { Text("Artefatos") }
        )
    }
}