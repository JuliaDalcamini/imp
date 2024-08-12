package com.julia.imp.artifact.details

import com.julia.imp.inspection.Inspection
import com.julia.imp.user.User
import kotlinx.datetime.Instant

data class ArtifactDetailsUiState(
    val loading: Boolean = true,
    val inspections: List<Inspection>? = null,
    val lastInspection: Instant? = null,
    val loadError: Boolean = false,
    val showInspectorPicker: Boolean = false,
    val availableInspectors: List<User>? = null,
    val inspectors: List<User> = emptyList(),
    val updatingInspectors: Boolean = false,
    val actionError: Boolean = false
)