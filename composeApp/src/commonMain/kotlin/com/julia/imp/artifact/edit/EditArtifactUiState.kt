package com.julia.imp.artifact.edit

import com.julia.imp.artifact.ArtifactType
import com.julia.imp.priority.Priority
import com.julia.imp.team.inspector.Inspector

data class EditArtifactUiState(
    val name: String = "",
    val type: ArtifactType? = null,
    val priority: Priority? = null,
    val inspectors: List<Inspector> = listOf(),
    val availableTypes: List<ArtifactType>? = null,
    val saving: Boolean = false,
    val saved: Boolean = false,
    val canSave: Boolean = false,
    val showInspectorPicker: Boolean = false,
    val availableInspectors: List<Inspector>? = null,
    val saveError: Boolean = false,
    val loadError: Boolean = false
)