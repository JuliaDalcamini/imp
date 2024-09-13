package com.julia.imp.project.manage

import com.julia.imp.project.Project

data class ManageProjectUiState(
    val loading: Boolean = false,
    val project: Project? = null,
    val canChangeStartDate: Boolean = false,
    val showRenameDialog: Boolean = false,
    val showDeleteDialog: Boolean = false,
    val showChangeMinInspectorsDialog: Boolean = false,
    val showChangeStartDateDialog: Boolean = false,
    val showChangeTargetDateDialog: Boolean = false,
    val showFinishDialog: Boolean = false,
    val projectToRename: Project? = null,
    val actionError: Boolean = false,
    val projectFinished: Boolean = false,
    val projectDeleted: Boolean = false,
    val loadError: Boolean = false
)
