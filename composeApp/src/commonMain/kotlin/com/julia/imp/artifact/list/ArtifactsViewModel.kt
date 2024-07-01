package com.julia.imp.artifact.list

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.julia.imp.artifact.ArtifactRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ArtifactsViewModel(
    private val repository: ArtifactRepository = ArtifactRepository()
) : ViewModel() {

    var uiState by mutableStateOf(ArtifactsUiState())
        private set

    fun getArtifacts(projectId: String) {
        viewModelScope.launch {
            uiState = uiState.copy(isLoading = true)

            val artifacts = repository.getArtifacts(projectId)

            uiState = uiState.copy(artifacts = artifacts, isLoading = false)
        }
    }

}