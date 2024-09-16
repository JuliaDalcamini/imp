package com.julia.imp.project.list

import com.julia.imp.project.Project
import com.julia.imp.project.ProjectFilter

data class ProjectsUiState(
    val loading: Boolean = false,
    val filter: ProjectFilter = ProjectFilter.Active,
    val projects: List<Project>? = null,
    val error: Boolean = false,
    val showCreateButton: Boolean = false,
    val showManageOption: Boolean = false,
    val projectToRename: Project? = null,
    val actionError: Boolean = false
)