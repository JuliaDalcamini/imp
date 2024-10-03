package com.julia.imp.artifact.list

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.julia.imp.artifact.Artifact
import com.julia.imp.artifact.ArtifactRepository
import com.julia.imp.common.session.requireSession
import com.julia.imp.project.Project
import kotlinx.coroutines.launch

class ArtifactsViewModel(
    private val repository: ArtifactRepository = ArtifactRepository()
) : ViewModel() {

    private lateinit var project: Project

    var uiState by mutableStateOf(ArtifactsUiState())
        private set

    fun initialize(project: Project) {
        this.project = project
        getArtifacts()
    }

    private fun getArtifacts() {
        viewModelScope.launch {
            try {
                val isAdmin = requireSession().isTeamAdmin

                uiState = ArtifactsUiState(
                    loading = true,
                    filter = uiState.filter,
                    showCreateButton = isAdmin && !project.finished
                )

                val artifacts = repository.getArtifacts(project.id, uiState.filter)

                uiState = uiState.copy(
                    entries = artifacts.map { artifact ->
                        ArtifactListEntry(
                            artifact = artifact,
                            showManagementOptions = isAdmin && !artifact.archived && !project.finished
                        )
                    },
                    empty = artifacts.isEmpty()
                )
            } catch (error: Throwable) {
                uiState = uiState.copy(error = true)
            } finally {
                uiState = uiState.copy(loading = false)
            }
        }
    }

    fun setFilter(filter: ArtifactFilter) {
        if (filter != uiState.filter) {
            uiState = uiState.copy(filter = filter)
            getArtifacts()
        }
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

                reload()
            } catch (error: Throwable) {
                uiState = uiState.copy(actionError = true)
            }
        }
    }

    fun dismissActionError() {
        uiState = uiState.copy(actionError = false)
    }

    fun reload() {
        getArtifacts()
    }
}