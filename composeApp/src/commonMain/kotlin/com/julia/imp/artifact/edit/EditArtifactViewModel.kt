package com.julia.imp.artifact.edit

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.julia.imp.artifact.Artifact
import com.julia.imp.artifact.ArtifactRepository
import com.julia.imp.artifact.ArtifactType
import com.julia.imp.common.session.requireTeam
import com.julia.imp.priority.Priority
import com.julia.imp.team.inspector.Inspector
import com.julia.imp.team.inspector.InspectorRepository
import kotlinx.coroutines.launch

class EditArtifactViewModel(
    private val repository: ArtifactRepository = ArtifactRepository(),
    private val inspectorRepository: InspectorRepository = InspectorRepository()
) : ViewModel() {

    private lateinit var artifact: Artifact

    var uiState by mutableStateOf(EditArtifactUiState())
        private set

    fun initialize(artifact: Artifact) {
        this.artifact = artifact

        uiState = uiState.copy(
            name = artifact.name,
            type = artifact.type,
            priority = artifact.priority,
            inspectors = artifact.inspectors
        )

        loadArtifactTypes()
    }

    private fun loadArtifactTypes() {
        viewModelScope.launch {
            uiState = uiState.copy(availableTypes = null)

            try {
                val types = repository.getArtifactTypes()
                uiState = uiState.copy(availableTypes = types)
            } catch (error: Throwable) {
                uiState = uiState.copy(loadError = true)
            }
        }
    }

    fun setName(name: String) {
        uiState = uiState.copy(name = name)
        updateSaveButtonState()
    }

    fun setType(type: ArtifactType) {
        uiState = uiState.copy(type = type)
        updateSaveButtonState()
    }

    fun setPriority(priority: Priority) {
        uiState = uiState.copy(priority = priority)
        updateSaveButtonState()
    }

    fun setInspectors(inspectors: List<Inspector>) {
        uiState = uiState.copy(inspectors = inspectors)
    }

    fun createArtifact() {
        viewModelScope.launch {
            uiState = uiState.copy(saving = true)

            try {
                repository.updateArtifact(getUpdatedArtifact())
                uiState = uiState.copy(saved = true)
            } catch (error: Throwable) {
                uiState = uiState.copy(saveError = true)
            } finally {
                uiState = uiState.copy(saving = false)
            }
        }
    }

    private fun getUpdatedArtifact() = artifact.copy(
        name = uiState.name,
        type = uiState.type ?: throw IllegalStateException("Type not set"),
        priority = uiState.priority ?: throw IllegalStateException("Priority not set"),
        inspectors = uiState.inspectors
    )

    fun dismissSaveError() {
        uiState = uiState.copy(saveError = false)
    }

    fun dismissLoadError() {
        uiState = uiState.copy(loadError = false)
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

    private fun updateSaveButtonState() {
        uiState = uiState.copy(
            canSave = uiState.run { name.isNotBlank() && type != null && priority != null }
        )
    }
}
