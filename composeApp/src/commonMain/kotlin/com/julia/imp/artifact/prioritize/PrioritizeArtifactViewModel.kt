package com.julia.imp.artifact.prioritize

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.julia.imp.artifact.ArtifactRepository
import com.julia.imp.priority.MoscowPrioritizer
import com.julia.imp.priority.MoscowPriority
import com.julia.imp.priority.Prioritizer
import com.julia.imp.priority.Priority
import com.julia.imp.priority.WiegersPrioritizer
import com.julia.imp.priority.WiegersPriority

class PrioritizeArtifactViewModel(
    private val repository: ArtifactRepository = ArtifactRepository()
) : ViewModel() {

    private lateinit var artifactId: String

    var uiState by mutableStateOf(PrioritizeArtifactUiState())
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