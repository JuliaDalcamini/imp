package com.julia.imp.artifact.prioritize

import com.julia.imp.artifact.Artifact
import com.julia.imp.priority.Priority

data class PrioritizeArtifactUiState (
    val priority: Priority? = null,
    val canPrioritize: Boolean = false,
    val prioritizeError: Boolean = false,
    val loadError: Boolean = false,
    val actionError: Boolean = false,
    val loading: Boolean = false,
    val saving: Boolean = false,
    val saveError: Boolean = false,
    val savedArtifact: Artifact? = null
)
