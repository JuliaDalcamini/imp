package com.julia.imp.defect

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.julia.imp.artifact.Artifact
import com.julia.imp.common.session.requireSession
import kotlinx.coroutines.launch

class DefectsViewModel(
    private val repository: DefectRepository = DefectRepository()
) : ViewModel() {

    private lateinit var artifact: Artifact

    var uiState by mutableStateOf(DefectsUiState())
        private set

    fun initialize(artifact: Artifact) {
        this.artifact = artifact
        getDefects()
    }

    fun getDefects() {
        viewModelScope.launch {
            try {
                val canFix = requireSession().isTeamAdmin || requireSession().isInspector

                uiState = DefectsUiState(
                    loading = true,
                    filter = uiState.filter,
                    showFixButton = canFix
                )

                val defects = repository.getDefects(
                    projectId = artifact.projectId,
                    artifactId = artifact.id,
                    filter = uiState.filter
                )

                uiState = uiState.copy(defects = defects)
            } catch (error: Throwable) {
                uiState = uiState.copy(error = true)
            } finally {
                uiState = uiState.copy(loading = false)
            }
        }
    }

    fun setFilter(filter: DefectFilter) {
        if (filter != uiState.filter) {
            uiState = uiState.copy(filter = filter)
            getDefects()
        }
    }

    fun askToFix(defect: Defect) {
        uiState = uiState.copy(defectToFix = defect)
    }

    fun dismissFixDialog() {
        uiState = uiState.copy(defectToFix = null)
    }

    fun fixDefect(defect: Defect) {
        viewModelScope.launch {
            try {
                repository.updateDefect(
                    projectId = artifact.projectId,
                    artifactId = artifact.id,
                    defectId = defect.id,
                    fixed = true
                )

                getDefects()
            } catch (error: Throwable) {
                uiState = uiState.copy(actionError = true)
            }
        }
    }

    fun dismissActionError() {
        uiState = uiState.copy(actionError = false)
    }
}