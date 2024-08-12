package com.julia.imp.artifact.create

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.julia.imp.artifact.ArtifactRepository
import com.julia.imp.artifact.ArtifactType
import com.julia.imp.common.session.requireTeam
import com.julia.imp.priority.MoscowPrioritizer
import com.julia.imp.priority.MoscowPriority
import com.julia.imp.priority.Prioritizer
import com.julia.imp.priority.Priority
import com.julia.imp.priority.WiegersPrioritizer
import com.julia.imp.priority.WiegersPriority
import com.julia.imp.team.inspector.InspectorRepository
import com.julia.imp.user.User
import kotlinx.coroutines.launch

class CreateArtifactViewModel(
    private val repository: ArtifactRepository = ArtifactRepository(),
    private val inspectorRepository: InspectorRepository = InspectorRepository()
) : ViewModel() {

    private lateinit var projectId: String

    var uiState by mutableStateOf(CreateArtifactUiState())
        private set

    fun initialize(projectId: String, prioritizer: Prioritizer) {
        this.projectId = projectId
        setPriority(getInitialPriority(prioritizer))
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
        updateCreateButtonState()
    }

    fun setType(type: ArtifactType) {
        uiState = uiState.copy(type = type)
        updateCreateButtonState()
    }

    fun setPriority(priority: Priority) {
        uiState = uiState.copy(priority = priority)
        updateCreateButtonState()
    }

    fun addInspector(inspector: User) {
        uiState = uiState.copy(inspectors = uiState.inspectors + inspector)
    }

    fun removeInspector(inspector: User) {
        uiState = uiState.copy(inspectors = uiState.inspectors - inspector)
    }

    fun createArtifact() {
        viewModelScope.launch {
            uiState = uiState.copy(loading = true)

            try {
                val artifact = repository.createArtifact(
                    projectId = projectId,
                    name = uiState.name,
                    inspectors = uiState.inspectors,
                    type = uiState.type ?: throw IllegalStateException("Type not set"),
                    priority = uiState.priority ?: throw IllegalStateException("Priority not set")
                )

                uiState = uiState.copy(createdArtifact = artifact)
            } catch (error: Throwable) {
                uiState = uiState.copy(createError = true)
            } finally {
                uiState = uiState.copy(loading = false)
            }
        }
    }

    fun dismissCreateError() {
        uiState = uiState.copy(createError = false)
    }

    fun dismissLoadError() {
        uiState = uiState.copy(createError = false)
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

    private fun getInitialPriority(prioritizer: Prioritizer) = when (prioritizer) {
        is MoscowPrioritizer -> MoscowPriority()
        is WiegersPrioritizer -> WiegersPriority()
    }

    private fun updateCreateButtonState() {
        uiState = uiState.copy(
            canCreate = uiState.run { name.isNotBlank() && type != null && priority != null }
        )
    }
}
