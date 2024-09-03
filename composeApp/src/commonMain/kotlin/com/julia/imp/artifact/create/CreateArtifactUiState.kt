package com.julia.imp.artifact.create

import com.julia.imp.artifact.Artifact
import com.julia.imp.artifact.ArtifactType
import com.julia.imp.priority.Priority
import com.julia.imp.user.User

data class CreateArtifactUiState(
    val name: String = "",
    val externalLink: String = "",
    val type: ArtifactType? = null,
    val priority: Priority? = null,
    val inspectors: List<User> = listOf(),
    val availableTypes: List<ArtifactType>? = null,
    val loading: Boolean = false,
    val createdArtifact: Artifact? = null,
    val canCreate: Boolean = false,
    val showInspectorPicker: Boolean = false,
    val availableInspectors: List<User>? = null,
    val createError: Boolean = false,
    val loadError: Boolean = false
)