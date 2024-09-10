package com.julia.imp.project.manage

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.julia.imp.artifact.ArtifactRepository
import com.julia.imp.artifact.list.ArtifactFilter
import com.julia.imp.inspection.InspectionRepository
import com.julia.imp.project.Project
import com.julia.imp.project.ProjectRepository
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate

class ManageProjectViewModel(
    private val repository: ProjectRepository = ProjectRepository(),
    private val artifactRepository: ArtifactRepository = ArtifactRepository(),
    private val inspectionRepository: InspectionRepository = InspectionRepository()
) : ViewModel() {

    var uiState by mutableStateOf(ManageProjectUiState())
        private set

    suspend fun initialize(project: Project) {
        canUpdateStartDate(project)
    }

    private suspend fun canUpdateStartDate(project: Project) {
        val artifacts = artifactRepository.getArtifacts(project.id, ArtifactFilter.All)
        artifacts.map { inspectionRepository.getInspections(project.id, it.id) }
        uiState = uiState.copy(canUpdateStartDate = artifacts.isEmpty())
    }

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

    fun showChangeStartDateDialog() {
        uiState = uiState.copy(showChangeStartDateDialog = true)
    }

    fun dismissChangeStartDateDialog() {
        uiState = uiState.copy(showChangeStartDateDialog = false)
    }

    fun rename(project: Project, newName: String) {
        viewModelScope.launch {
            try {
                repository.updateProject(
                    projectId = project.id,
                    newName = newName,
                    newStartDate = project.startDate,
                    newTargetDate = project.targetDate,
                    newMinInspectors = project.minInspectors
                )
            } catch (error: Throwable) {
                uiState = uiState.copy(actionError = true)
            }
        }
    }

    fun changeStartDate(project: Project, newStartDate: LocalDate) {
        viewModelScope.launch {
            try {
                repository.updateProject(
                    projectId = project.id,
                    newName = project.name,
                    newStartDate = newStartDate,
                    newTargetDate = project.targetDate,
                    newMinInspectors = project.minInspectors
                )
            } catch (error: Throwable) {
                uiState = uiState.copy(actionError = true)
            }
        }
    }

    fun changeTargetDate(project: Project, newTargetDate: LocalDate) {
        viewModelScope.launch {
            try {
                repository.updateProject(
                    projectId = project.id,
                    newName = project.name,
                    newStartDate = project.startDate,
                    newTargetDate = newTargetDate,
                    newMinInspectors = project.minInspectors
                )
            } catch (error: Throwable) {
                uiState = uiState.copy(actionError = true)
            }
        }
    }

    fun changeMinInspectors(project: Project, newMinInspectors: Int) {
        viewModelScope.launch {
            try {
                repository.updateProject(
                    projectId = project.id,
                    newName = project.name,
                    newStartDate = project.startDate,
                    newTargetDate = project.targetDate,
                    newMinInspectors = newMinInspectors
                )
            } catch (error: Throwable) {
                uiState = uiState.copy(actionError = true)
            }
        }
    }
}
