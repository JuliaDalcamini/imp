package com.julia.imp.inspection.details

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.julia.imp.common.session.requireSession
import com.julia.imp.inspection.InspectionRepository
import kotlinx.coroutines.launch

class InspectionDetailsViewModel(
    private val repository: InspectionRepository = InspectionRepository()
) : ViewModel() {

    private lateinit var inspectionId: String
    private lateinit var artifactId: String
    private lateinit var projectId: String

    var uiState by mutableStateOf(InspectionDetailsUiState())
        private set

    fun initialize(inspectionId: String, artifactId: String, projectId: String) {
        this.inspectionId = inspectionId
        this.artifactId = artifactId
        this.projectId = projectId

        loadInspection()
    }

    private fun loadInspection() {
        viewModelScope.launch {
            uiState = InspectionDetailsUiState(loading = true)

            try {
                val inspection = repository.getInspection(
                    projectId = projectId,
                    artifactId = artifactId,
                    inspectionId = inspectionId
                )

                uiState = uiState.copy(
                    inspection = inspection,
                    showCosts = !requireSession().isInspector
                )
            } catch (error: Throwable) {
                uiState = uiState.copy(loadError = true)
            } finally {
                uiState = uiState.copy(loading = false)
            }
        }
    }

    fun dismissLoadError() {
        uiState = uiState.copy(loadError = false)
    }
}