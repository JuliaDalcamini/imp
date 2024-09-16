package com.julia.imp.project.list

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.julia.imp.common.session.requireSession
import com.julia.imp.common.session.requireTeam
import com.julia.imp.project.ProjectFilter
import com.julia.imp.project.ProjectRepository
import kotlinx.coroutines.launch

class ProjectsViewModel(
    private val repository: ProjectRepository = ProjectRepository()
) : ViewModel() {

    var uiState by mutableStateOf(ProjectsUiState())
        private set

    fun getProjects(filter: ProjectFilter = uiState.filter) {
        viewModelScope.launch {
            try {
                val isAdmin = requireSession().isTeamAdmin

                uiState = ProjectsUiState(
                    loading = true,
                    filter = filter,
                    showCreateButton = isAdmin,
                    showManageOption = isAdmin
                )

                val projects = repository.getProjects(requireTeam().id, uiState.filter)

                uiState = uiState.copy(projects = projects)
            } catch (error: Throwable) {
                uiState = uiState.copy(error = true)
            } finally {
                uiState = uiState.copy(loading = false)
            }
        }
    }

    fun setFilter(filter: ProjectFilter) {
        if (filter != uiState.filter) {
            uiState = uiState.copy(filter = filter)
            getProjects()
        }
    }

    fun dismissActionError() {
        uiState = uiState.copy(actionError = false)
    }
}