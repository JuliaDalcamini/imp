package com.julia.imp.project.list

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.julia.imp.common.session.requireSession
import com.julia.imp.project.Project
import com.julia.imp.project.ProjectRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ProjectsViewModel(
    private val repository: ProjectRepository = ProjectRepository()
) : ViewModel() {
    
    var uiState by mutableStateOf(ProjectsUiState())
        private set

    fun getProjects() {
        viewModelScope.launch {
            uiState = uiState.copy(isLoading = true, error = false)

            try {
                val projects = repository.getProjects(requireSession().team.id)

                uiState = uiState.copy(
                    isLoading = false,
                    projects = projects,
                    showCreateButton = requireSession().isTeamAdmin
                )
            } catch (error: Throwable) {
                uiState = uiState.copy(error = true)
            } finally {
                uiState = uiState.copy(isLoading = false)
            }
        }
    }

    fun askToDelete(project: Project) {
        uiState = uiState.copy(projectToDelete = project)
    }

    fun dismissDeletion() {
        uiState = uiState.copy(projectToDelete = null)
    }

    fun delete(project: Project) {
        viewModelScope.launch {
            try {
                repository.deleteProject(project.id)
                getProjects()
            } catch (error: Throwable) {
                // TODO: Handle error
                error.printStackTrace()
            }
        }
    }

    fun askToRename(project: Project) {
        uiState = uiState.copy(projectToRename = project)
    }

    fun dismissRenaming() {
        uiState = uiState.copy(projectToRename = null)
    }

    fun rename(project: Project, newName: String) {
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