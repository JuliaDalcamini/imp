package com.julia.imp.artifact.edit

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.julia.imp.artifact.Artifact
import com.julia.imp.artifact.ArtifactRepository
import com.julia.imp.common.session.requireTeam
import com.julia.imp.team.inspector.InspectorRepository
import com.julia.imp.user.User
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
            version = artifact.currentVersion,
            externalLink = artifact.externalLink,
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

    fun setCurrentVersion(currentVersion: String) {
        uiState = uiState.copy(
            version = currentVersion,
            needsVersionChange = false
        )

        updateSaveButtonState()
    }

    fun setExternalLink(externalLink: String) {
        uiState = uiState.copy(externalLink = externalLink)
        updateSaveButtonState()
    }

    fun addInspector(inspector: User) {
        uiState = uiState.copy(inspectors = uiState.inspectors + inspector)
        updateSaveButtonState()
    }

    fun removeInspector(inspector: User) {
        uiState = uiState.copy(inspectors = uiState.inspectors - inspector)
        updateSaveButtonState()
    }

    fun updateArtifact() {
        viewModelScope.launch {
            uiState = uiState.copy(saving = true)

            try {
                val savedArtifact = repository.updateArtifact(getUpdatedArtifact())
                uiState = uiState.copy(savedArtifact = savedArtifact)
            } catch (error: Throwable) {
                uiState = uiState.copy(saveError = true)
            } finally {
                uiState = uiState.copy(saving = false)
            }
        }
    }

    private fun getUpdatedArtifact() = artifact.copy(
        name = uiState.name,
        currentVersion = uiState.version,
        externalLink = uiState.externalLink,
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

    fun askToSave() {
        if (validateChanges()) {
            uiState = uiState.copy(showSaveDialog = true)
        }
    }

    fun dismissSaveDialog() {
        uiState = uiState.copy(showSaveDialog = false)
    }

    private fun validateChanges(): Boolean {
        uiState = uiState.copy(needsVersionChange = needsVersionChange())
        updateSaveButtonState()

        return !uiState.needsVersionChange
    }

    private fun needsVersionChange() =
        uiState.version == artifact.currentVersion &&
                (uiState.name != artifact.name || uiState.externalLink != artifact.externalLink)

    private fun updateSaveButtonState() {
        uiState = uiState.copy(
            canSave = uiState.run { name.isNotBlank() && !needsVersionChange }
        )
    }
}
