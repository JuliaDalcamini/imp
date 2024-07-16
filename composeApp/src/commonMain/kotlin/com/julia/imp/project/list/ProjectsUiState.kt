package com.julia.imp.project.list

import com.julia.imp.project.Project

data class ProjectsUiState(
    val loading: Boolean = false,
    val projects: List<Project>? = null,
    val error: Boolean = false,
    val showCreateButton: Boolean = false,
    val showRenameOption: Boolean = false,
    val showDeleteOption: Boolean = false,
    val projectToRename: Project? = null,
    val projectToDelete: Project? = null,
    val projectToGenerateReport: Project? = null,
    val actionError: Boolean = false
)