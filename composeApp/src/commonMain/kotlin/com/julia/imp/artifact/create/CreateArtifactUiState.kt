package com.julia.imp.artifact.create

import com.julia.imp.artifact.ArtifactType
import com.julia.imp.priority.Priority
import com.julia.imp.team.inspector.Inspector

data class CreateArtifactUiState(
    val name: String = "",
    val type: ArtifactType? = null,
    val priority: Priority? = null,
    val inspectors: List<Inspector> = listOf(),
    val availableTypes: List<ArtifactType>? = null,
    val loading: Boolean = false,
    val created: Boolean = false,
    val canCreate: Boolean = false,
    val showInspectorPicker: Boolean = false,
    val availableInspectors: List<Inspector>? = null,
    val createError: Boolean = false,
    val loadError: Boolean = false
)