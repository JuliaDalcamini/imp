package com.julia.imp.artifact.list

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.julia.imp.artifact.Artifact
import com.julia.imp.artifact.ArtifactRepository
import com.julia.imp.common.session.requireSession
import kotlinx.coroutines.launch

class ArtifactsViewModel(
    private val repository: ArtifactRepository = ArtifactRepository()
) : ViewModel() {

    var uiState by mutableStateOf(ArtifactsUiState())
        private set

    fun getArtifacts(projectId: String, filter: ArtifactFilter) {
        viewModelScope.launch {
            try {
                val isAdmin = requireSession().isTeamAdmin

                uiState = ArtifactsUiState(
                    loading = true,
                    filter = filter,
                    showCreateButton = isAdmin
                )

                val artifacts = repository.getArtifacts(projectId, filter)

                uiState = uiState.copy(
                    entries = artifacts.map { artifact ->
                        ArtifactListEntry(
                            artifact = artifact,
                            showOptions = isAdmin && !artifact.archived
                        )
                    }
                )
            } catch (error: Throwable) {
                uiState = uiState.copy(error = true)
            } finally {
                uiState = uiState.copy(loading = false)
            }
        }
    }

    fun setFilter(filter: ArtifactFilter) {
        uiState = uiState.copy(filter = filter)
    }

    fun askToArchive(artifact: Artifact) {
        uiState = uiState.copy(artifactToArchive = artifact)
    }

    fun dismissArchiving() {
        uiState = uiState.copy(artifactToArchive = null)
    }

    fun archive(artifact: Artifact) {
        viewModelScope.launch {
            try {
                repository.archiveArtifact(
                    projectId = artifact.projectId,
                    artifactId = artifact.id
                )

                getArtifacts(artifact.projectId, uiState.filter)
            } catch (error: Throwable) {
                uiState = uiState.copy(actionError = true)
            }
        }
    }

    fun dismissActionError() {
        uiState = uiState.copy(actionError = false)
    }
}