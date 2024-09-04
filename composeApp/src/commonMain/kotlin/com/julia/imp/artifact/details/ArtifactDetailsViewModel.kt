package com.julia.imp.artifact.details

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.julia.imp.artifact.Artifact
import com.julia.imp.artifact.ArtifactRepository
import com.julia.imp.common.session.requireSession
import com.julia.imp.common.session.requireTeam
import com.julia.imp.inspection.InspectionRepository
import com.julia.imp.team.inspector.InspectorRepository
import com.julia.imp.user.User
import kotlinx.coroutines.launch

class ArtifactDetailsViewModel(
    private val artifactRepository: ArtifactRepository = ArtifactRepository(),
    private val inspectionRepository: InspectionRepository = InspectionRepository(),
    private val inspectorRepository: InspectorRepository = InspectorRepository()
) : ViewModel() {

    private lateinit var artifact: Artifact

    var uiState by mutableStateOf(ArtifactDetailsUiState())
        private set

    fun initialize(artifact: Artifact) {
        this.artifact = artifact

        uiState = uiState.copy(
            inspectors = artifact.inspectors
        )

        updateInspectButtonState()
        loadInspections()
    }

    private fun updateInspectButtonState() {
        uiState = uiState.copy(
            canInspect = !artifact.archived && isUserInInspectorList()
        )
    }

    fun getLoggedUserId(): String = requireSession().userId

    fun isUserInInspectorList(): Boolean {
        val userId = getLoggedUserId()
        return uiState.inspectors.any { it.id == userId }
    }

    private fun loadInspections() {
        viewModelScope.launch {
            uiState = uiState.copy(loading = true)

            try {
                val isInspector = requireSession().isInspector

                uiState = uiState.copy(
                    isInspector = isInspector
                )

                val inspections = inspectionRepository.getInspections(
                    projectId = artifact.projectId,
                    artifactId = artifact.id
                )

                uiState = uiState.copy(
                    inspections = inspections,
                    lastInspection = inspections.maxOfOrNull { it.createdAt }
                )
            } catch (error: Throwable) {
                uiState = uiState.copy(loadError = true)
            } finally {
                uiState = uiState.copy(loading = false)
            }
        }
    }

    fun dismissLoadError() {
        uiState = uiState.copy(loadError = false)
    }

    fun dismissActionError() {
        uiState = uiState.copy(actionError = false)
    }

    fun addInspector(inspector: User) {
        updateInspectors(uiState.inspectors + inspector)
    }

    fun removeInspector(inspector: User) {
        updateInspectors(uiState.inspectors - inspector)
    }

    private fun updateInspectors(inspectors: List<User>) {
        viewModelScope.launch {
            uiState = uiState.copy(updatingInspectors = true)

            try {
                val updatedArtifact = artifactRepository.updateArtifact(
                    artifact.copy(inspectors = inspectors)
                )

                uiState = uiState.copy(inspectors = updatedArtifact.inspectors)
                updateInspectButtonState()
            } catch (error: Throwable) {
                uiState = uiState.copy(actionError = true)
            } finally {
                uiState = uiState.copy(updatingInspectors = false)
            }
        }
    }

    fun openInspectorPicker() {
        uiState = uiState.copy(
            showInspectorPicker = true,
            availableInspectors = null
        )

        loadAvailableInspectors()
    }

    private fun loadAvailableInspectors() {
        viewModelScope.launch {
            uiState = uiState.copy(availableInspectors = null)

            try {
                val inspectors = inspectorRepository.getInspectors(requireTeam().id)
                uiState = uiState.copy(availableInspectors = inspectors - uiState.inspectors.toSet())
            } catch (error: Throwable) {
                uiState = uiState.copy(loadError = true, showInspectorPicker = false)
            }
        }
    }

    fun dismissInspectorPicker() {
        uiState = uiState.copy(
            showInspectorPicker = false,
            availableInspectors = null
        )
    }
}
