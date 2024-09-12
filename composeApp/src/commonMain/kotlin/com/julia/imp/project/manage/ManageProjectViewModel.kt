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

    fun initialize(projectId: String) {
        viewModelScope.launch {
            try {
                val project = repository.getProject(projectId)
                val artifacts = artifactRepository.getArtifacts(project.id, ArtifactFilter.All)

                val inspections = artifacts.flatMap {
                    inspectionRepository.getInspections(project.id, it.id)
                }

                uiState = uiState.copy(
                    project = project,
                    canChangeStartDate = inspections.isEmpty()
                )
            } catch (error: Throwable) {
                uiState = uiState.copy(loadError = true)
            } finally {
                uiState = uiState.copy(loading = false)
            }
        }
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

    fun showFinishDialog() {
        uiState = uiState.copy(showFinishDialog = true)
    }

    fun dismissFinishDialog() {
        uiState = uiState.copy(showFinishDialog = false)
    }

    fun dismissLoadError() {
        uiState = uiState.copy(loadError = false)
    }

    fun rename(project: Project, newName: String) {
        viewModelScope.launch {
            uiState = uiState.copy(loading = true)

            try {
                updateProject(project = project, name = newName)
            } catch (error: Throwable) {
                uiState = uiState.copy(actionError = true)
            } finally {
                uiState = uiState.copy(loading = false)
            }
        }
    }

    fun changeStartDate(project: Project, newStartDate: LocalDate) {
        viewModelScope.launch {
            uiState = uiState.copy(loading = true)

            try {
                updateProject(project = project, startDate = newStartDate)
            } catch (error: Throwable) {
                uiState = uiState.copy(actionError = true)
            } finally {
                uiState = uiState.copy(loading = false)
            }
        }
    }

    fun changeTargetDate(project: Project, newTargetDate: LocalDate) {
        viewModelScope.launch {
            uiState = uiState.copy(loading = true)

            try {
                updateProject(project = project, targetDate = newTargetDate)
            } catch (error: Throwable) {
                uiState = uiState.copy(actionError = true)
            } finally {
                uiState = uiState.copy(loading = false)
            }
        }
    }

    fun changeMinInspectors(project: Project, newMinInspectors: Int) {
        viewModelScope.launch {
            uiState = uiState.copy(loading = true)

            try {
                updateProject(project = project, minInspectors = newMinInspectors)
            } catch (error: Throwable) {
                uiState = uiState.copy(actionError = true)
            } finally {
                uiState = uiState.copy(loading = false)
            }
        }
    }

    fun finishProject(project: Project) {
        viewModelScope.launch {
            uiState = uiState.copy(loading = true)

            try {
                updateProject(project = project, finished = true)
                uiState = uiState.copy(projectFinished = true)
            } catch (error: Throwable) {
                uiState = uiState.copy(actionError = true)
            } finally {
                uiState = uiState.copy(loading = false)
            }
        }
    }

    private suspend fun updateProject(
        project: Project,
        name: String = project.name,
        startDate: LocalDate = project.startDate,
        targetDate: LocalDate = project.targetDate,
        minInspectors: Int = project.minInspectors,
        finished: Boolean = project.finished
    ) {
        val updatedProject = repository.updateProject(
            projectId = project.id,
            name = name,
            startDate = startDate,
            targetDate = targetDate,
            minInspectors = minInspectors,
            finished = finished
        )

        uiState = uiState.copy(project = updatedProject)
    }
}
