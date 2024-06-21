package com.julia.imp.project.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.julia.imp.project.Project
import com.julia.imp.project.ProjectRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProjectsViewModel(
    private val repository: ProjectRepository = ProjectRepository()
) : ViewModel() {
    
    private val mutableUiState = MutableStateFlow(ProjectsUiState())
    val uiState = mutableUiState.asStateFlow()

    fun getProjects(teamId: String) {
        viewModelScope.launch {
            try {
                val projects = repository.getProjects(teamId)

                mutableUiState.update {
                    it.copy(isLoading = false, projects = projects)
                }
            } catch (error: Throwable) {
                mutableUiState.update { it.copy(error = error.message) }
            } finally {
                mutableUiState.update { it.copy(isLoading = false) }
            }
        }
    }

    fun askToDeleteProject(project: Project) {
        mutableUiState.update { it.copy(projectToDelete = project) }
    }

    fun dismissProjectDeletion() {
        mutableUiState.update { it.copy(projectToDelete = null) }
    }

    fun deleteProject(project: Project, teamId: String) {
        viewModelScope.launch {
            try {
                repository.deleteProject(project.id)

                // TODO: Remove teamId parameter, handle success and loading
                getProjects(teamId)
            } catch (error: Throwable) {
                // TODO: Handle error
            }
        }
    }
}