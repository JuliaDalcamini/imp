package com.julia.imp.project.list

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.julia.imp.common.session.SessionManager
import com.julia.imp.common.session.requireSession
import com.julia.imp.project.Project
import com.julia.imp.project.ProjectRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProjectsViewModel(
    private val repository: ProjectRepository = ProjectRepository()
) : ViewModel() {
    
    var uiState by mutableStateOf(ProjectsUiState())
        private set

    fun getProjects() {
        viewModelScope.launch {
            try {
                val projects = repository.getProjects(requireSession().team.id)

                uiState = uiState.copy(
                    isLoading = false,
                    projects = projects,
                    showCreateButton = requireSession().isTeamAdmin
                )
            } catch (error: Throwable) {
                uiState = uiState.copy(error = error.message)
            } finally {
                uiState = uiState.copy(isLoading = false)
            }
        }
    }

    fun askToDeleteProject(project: Project) {
        uiState = uiState.copy(projectToDelete = project)
    }

    fun dismissProjectDeletion() {
        uiState = uiState.copy(projectToDelete = null)
    }

    fun deleteProject(project: Project) {
        viewModelScope.launch {
            try {
                repository.deleteProject(project.id)
                getProjects()
            } catch (error: Throwable) {
                // TODO: Handle error
            }
        }
    }

    fun askToRenameProject(project: Project) {
        uiState = uiState.copy(projectToRename = project)
    }

    fun dismissProjectRenaming() {
        uiState = uiState.copy(projectToRename = null)
    }

    fun renameProject(project: Project, newName: String) {
        viewModelScope.launch {
            try {
                repository.renameProject(project.id, newName)
                getProjects()
            } catch (error: Throwable) {
                // TODO: Handle error
                error.printStackTrace()
            }
        }
    }
}