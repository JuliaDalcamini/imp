package com.julia.imp.priority

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.julia.imp.artifact.ArtifactRepository

class PrioritizerViewModel(
    private val repository: ArtifactRepository = ArtifactRepository()
) : ViewModel() {

    private lateinit var artifactId: String

    var uiState by mutableStateOf(PrioritizerUiState())
        private set

    fun initialize(artifactId: String, prioritizer: Prioritizer) {
        this.artifactId = artifactId
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

    }

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