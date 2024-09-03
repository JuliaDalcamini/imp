package com.julia.imp.artifact.prioritize

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.julia.imp.artifact.Artifact
import com.julia.imp.artifact.ArtifactRepository
import com.julia.imp.priority.MoscowPrioritizer
import com.julia.imp.priority.MoscowPriority
import com.julia.imp.priority.Prioritizer
import com.julia.imp.priority.Priority
import com.julia.imp.priority.WiegersPrioritizer
import com.julia.imp.priority.WiegersPriority
import kotlinx.coroutines.launch

class PrioritizeArtifactViewModel(
    private val repository: ArtifactRepository = ArtifactRepository()
) : ViewModel() {

    private lateinit var artifact: Artifact

    var uiState by mutableStateOf(PrioritizeArtifactUiState())
        private set

    fun initialize(artifact: Artifact, prioritizer: Prioritizer) {
        this.artifact = artifact
        setPriority(getInitialPriority(prioritizer))
    }

    fun setPriority(priority: Priority) {
        uiState = uiState.copy(priority = priority)
    }

    private fun getInitialPriority(prioritizer: Prioritizer) = when (prioritizer) {
        is MoscowPrioritizer -> MoscowPriority()
        is WiegersPrioritizer -> WiegersPriority()
    }

    fun prioritizeArtifact() {
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
        priority = uiState.priority ?: throw IllegalStateException("Priority not set")
    )

    fun dismissPrioritizeError() {
        uiState = uiState.copy(prioritizeError = false)
    }

    fun dismissLoadError() {
        uiState = uiState.copy(prioritizeError = false)
    }

    fun dismissActionError() {
        uiState = uiState.copy(actionError = false)
    }

//    fun isWiegersSumValid(): Boolean {
//        val prioritizer = uiState.prioritizer as? WiegersPrioritizer ?: return false
//        val totalWeight = prioritizer.userValueWeight + prioritizer.complexityWeight + prioritizer.impactWeight
//        return totalWeight == 10.0
//    }
}