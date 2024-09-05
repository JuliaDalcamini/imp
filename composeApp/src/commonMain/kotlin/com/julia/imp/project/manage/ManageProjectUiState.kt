package com.julia.imp.project.manage

import com.julia.imp.project.Project

data class ManageProjectUiState(
    val minInspectors: Int = 2,
    val showRenameDialog: Boolean = false,
    val showChangeMinInspectorsDialog: Boolean = false,
    val showChangeTargetDateDialog: Boolean = false,
    val projectToRename: Project? = null,
    val actionError: Boolean = false,
    val updatedProject: Project? = null,
    val error: Boolean = false
)
