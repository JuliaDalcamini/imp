package com.julia.imp.artifact.details

import com.julia.imp.artifact.Artifact
import com.julia.imp.inspection.Inspection
import com.julia.imp.user.User
import kotlinx.datetime.Instant

data class ArtifactDetailsUiState(
    val loading: Boolean = true,
    val artifact: Artifact? = null,
    val inspections: List<Inspection>? = null,
    val lastInspection: Instant? = null,
    val canInspect: Boolean = false,
    val loadError: Boolean = false,
    val showEditButton: Boolean = false,
    val showInspectorPicker: Boolean = false,
    val availableInspectors: List<User>? = null,
    val canEditInspectors: Boolean = false,
    val updatingInspectors: Boolean = false,
    val showCosts: Boolean = false,
    val showReinspectAlert: Boolean = false,
    val actionError: Boolean = false
)