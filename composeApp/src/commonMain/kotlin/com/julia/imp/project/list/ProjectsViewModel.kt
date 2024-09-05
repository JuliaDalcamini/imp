package com.julia.imp.project.list

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.julia.imp.common.session.requireSession
import com.julia.imp.common.session.requireTeam
import com.julia.imp.project.Project
import com.julia.imp.project.ProjectRepository
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate

class ProjectsViewModel(
    private val repository: ProjectRepository = ProjectRepository()
) : ViewModel() {
    
    var uiState by mutableStateOf(ProjectsUiState())
        private set

    fun getProjects() {
        viewModelScope.launch {
            try {
                val isAdmin = requireSession().isTeamAdmin

                uiState = ProjectsUiState(
                    loading = true,
                    showCreateButton = isAdmin,
                    showRenameOption = isAdmin,
                    showDeleteOption = isAdmin
                )

                val projects = repository.getProjects(requireTeam().id)

                uiState = uiState.copy(projects = projects)
            } catch (error: Throwable) {
                uiState = uiState.copy(error = true)
            } finally {
                uiState = uiState.copy(loading = false)
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
                uiState = uiState.copy(actionError = true)
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
                repository.updateProject(project.id, newName, project.targetDate, project.minInspectors)
                getProjects()
            } catch (error: Throwable) {
                uiState = uiState.copy(actionError = true)
            }
        }
    }

    fun changeTargetDate(project: Project, newTargetDate: LocalDate) {
        viewModelScope.launch {
            try {
                repository.updateProject(project.id, project.name, newTargetDate, project.minInspectors)
                getProjects()
            } catch (error: Throwable) {
                uiState = uiState.copy(actionError = true)
            }
        }
    }

    fun changeMinInspectors(project: Project, newMinInspectors: Int) {
        viewModelScope.launch {
            try {
                repository.updateProject(project.id, project.name, project.targetDate, newMinInspectors)
                getProjects()
            } catch (error: Throwable) {
                uiState = uiState.copy(actionError = true)
            }
        }
    }

    fun generateReport(project: Project) {
        uiState = uiState.copy(projectToGenerateReport = project)
    }

    fun onReportOpened() {
        uiState = uiState.copy(projectToGenerateReport = null)
    }

    fun dismissActionError() {
        uiState = uiState.copy(actionError = false)
    }
}