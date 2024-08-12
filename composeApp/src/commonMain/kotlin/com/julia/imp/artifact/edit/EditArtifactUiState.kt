package com.julia.imp.artifact.edit

import com.julia.imp.artifact.Artifact
import com.julia.imp.artifact.ArtifactType
import com.julia.imp.priority.Priority
import com.julia.imp.user.User

data class EditArtifactUiState(
    val name: String = "",
    val type: ArtifactType? = null,
    val priority: Priority? = null,
    val inspectors: List<User> = listOf(),
    val availableTypes: List<ArtifactType>? = null,
    val saving: Boolean = false,
    val savedArtifact: Artifact? = null,
    val canSave: Boolean = false,
    val showInspectorPicker: Boolean = false,
    val availableInspectors: List<User>? = null,
    val saveError: Boolean = false,
    val loadError: Boolean = false
)