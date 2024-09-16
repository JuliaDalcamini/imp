package com.julia.imp.project.dashboard

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class DashboardViewModel(
    private val repository: DashboardRepository = DashboardRepository()
) : ViewModel() {

    var uiState by mutableStateOf(DashboardUiState())
        private set

    fun getDashboardData(projectId: String) {
        viewModelScope.launch {
            uiState = uiState.copy(loading = true, error = false)

            try {
                val data = repository.getDashboardData(projectId)
                uiState = uiState.copy(data = data)
            } catch (error: Throwable) {
                uiState = uiState.copy(error = true)
            } finally {
                uiState = uiState.copy(loading = false)
            }
        }
    }

    fun generateReport() {
        uiState = uiState.copy(generateReport = true)
    }

    fun onReportOpened() {
        uiState = uiState.copy(generateReport = false)
    }
}
