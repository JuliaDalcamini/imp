package com.julia.imp.project.manage

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.julia.imp.project.Project
import com.julia.imp.project.ProjectRepository
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate

class ManageProjectViewModel(
    private val repository: ProjectRepository = ProjectRepository()
) : ViewModel() {

    var uiState by mutableStateOf(ManageProjectUiState())
        private set

    fun showRenameDialog() {
        uiState = uiState.copy(showRenameDialog = true)
    }

    fun dismissRenameDialog() {
        uiState = uiState.copy(showRenameDialog = false)
    }

    fun showChangeMinInspectorsDialog() {
        uiState = uiState.copy(showChangeMinInspectorsDialog = true)
    }

    fun dismissChangeMinInspectorsDialog() {
        uiState = uiState.copy(showChangeMinInspectorsDialog = false)
    }

    fun showChangeTargetDateDialog() {
        uiState = uiState.copy(showChangeTargetDateDialog = true)
    }

    fun dismissChangeTargetDateDialog() {
        uiState = uiState.copy(showChangeTargetDateDialog = false)
    }

    fun askToRename(project: Project) {
        uiState = uiState.copy(projectToRename = project)
    }

    fun dismissRenaming() {
        uiState = uiState.copy(projectToRename = null)
    }

    fun setInspectorCount(count: Int) {
        uiState = uiState.copy(minInspectors = count)
    }

    fun rename(project: Project, newName: String) {
        viewModelScope.launch {
            try {
                repository.updateProject(project.id, newName, project.targetDate, project.minInspectors)
            } catch (error: Throwable) {
                uiState = uiState.copy(actionError = true)
            }
        }
    }

    fun changeTargetDate(project: Project, newTargetDate: LocalDate) {
        viewModelScope.launch {
            try {
                repository.updateProject(project.id, project.name, newTargetDate, project.minInspectors)
            } catch (error: Throwable) {
                uiState = uiState.copy(actionError = true)
            }
        }
    }

    fun changeMinInspectors(project: Project, newMinInspectors: Int) {
        viewModelScope.launch {
            try {
                repository.updateProject(project.id, project.name, project.targetDate, newMinInspectors)
            } catch (error: Throwable) {
                uiState = uiState.copy(actionError = true)
            }
        }
    }
}
