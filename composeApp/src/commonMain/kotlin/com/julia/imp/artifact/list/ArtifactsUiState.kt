package com.julia.imp.artifact.list

import com.julia.imp.project.Project

data class ArtifactsUiState(
    val isLoading: Boolean = false,
    val artifacts: List<ArtifactListEntry>? = null,
    val isError: Boolean = false,
    val artifactToArchive: ArtifactListEntry? = null,
    val showCreateButton: Boolean = false
)
