package com.julia.imp.artifact.edit

import com.julia.imp.artifact.Artifact
import com.julia.imp.artifact.ArtifactType
import com.julia.imp.user.User

data class EditArtifactUiState(
    val name: String = "",
    val version: String = "",
    val externalLink: String = "",
    val inspectors: List<User> = listOf(),
    val availableTypes: List<ArtifactType>? = null,
    val showSaveDialog: Boolean = false,
    val saving: Boolean = false,
    val savedArtifact: Artifact? = null,
    val canSave: Boolean = false,
    val showInspectorPicker: Boolean = false,
    val availableInspectors: List<User>? = null,
    val needsVersionChange: Boolean = false,
    val saveError: Boolean = false,
    val loadError: Boolean = false
)