package com.julia.imp.inspection

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.julia.imp.common.session.requireSession
import kotlinx.coroutines.launch

class InspectionDetailsViewModel(
    private val inspectionRepository: InspectionRepository = InspectionRepository()
) : ViewModel() {

    private lateinit var projectId: String
    private lateinit var artifactId: String
    private lateinit var inspectionId: String

    var uiState by mutableStateOf(InspectionDetailsUiState())
        private set

    fun initialize(projectId: String, artifactId: String, inspectionId: String) {
        this.projectId = projectId
        this.artifactId = artifactId
        this.inspectionId = inspectionId
        loadInspection()
    }

    private fun loadInspection() {
        viewModelScope.launch {
            uiState = InspectionDetailsUiState(loading = true)

            try {
                val inspection = inspectionRepository.getInspection(
                    projectId = projectId,
                    artifactId = artifactId,
                    inspectionId = inspectionId
                )

                onInspectionLoaded(inspection)
            } catch (error: Throwable) {
                uiState = uiState.copy(loadError = true)
            } finally {
                uiState = uiState.copy(loading = false)
            }
        }
    }

    private fun onInspectionLoaded(inspection: Inspection) {
        val isInspector = requireSession().isInspector

        uiState = uiState.copy(
            inspection = inspection,
            showCosts = !isInspector
        )
    }

    fun dismissLoadError() {
        uiState = uiState.copy(loadError = false)
    }
}