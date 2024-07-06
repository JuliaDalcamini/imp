package com.julia.imp.project.list

import com.julia.imp.project.Project

data class ProjectsUiState(
    val isLoading: Boolean = false,
    val projects: List<Project>? = null,
    val error: Boolean = false,
    val showCreateButton: Boolean = false,
    val projectToDelete: Project? = null,
    val projectToRename: Project? = null,
    val projectToGenerateReport: Project? = null,
)