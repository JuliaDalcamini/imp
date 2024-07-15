package com.julia.imp.artifact.list

import com.julia.imp.artifact.Artifact

data class ArtifactsUiState(
    val filter: ArtifactFilter = ArtifactFilter.Active,
    val isLoading: Boolean = false,
    val entries: List<ArtifactListEntry>? = null,
    val isError: Boolean = false,
    val showCreateButton: Boolean = false,
    val artifactToArchive: Artifact? = null
)
