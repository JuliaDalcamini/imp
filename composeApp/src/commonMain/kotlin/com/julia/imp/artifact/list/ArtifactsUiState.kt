package com.julia.imp.artifact.list

import com.julia.imp.artifact.Artifact

data class ArtifactsUiState(
    val filter: ArtifactFilter = ArtifactFilter.Active,
    val loading: Boolean = false,
    val entries: List<ArtifactListEntry>? = null,
    val error: Boolean = false,
    val showCreateButton: Boolean = false,
    val artifactToArchive: Artifact? = null,
    val actionError: Boolean = false
)
