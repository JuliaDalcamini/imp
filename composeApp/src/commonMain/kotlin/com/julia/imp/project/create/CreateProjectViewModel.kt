package com.julia.imp.project.create

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.julia.imp.common.session.requireTeam
import com.julia.imp.priority.Prioritizer
import com.julia.imp.priority.WiegersPrioritizer
import com.julia.imp.project.ProjectRepository
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate

class CreateProjectViewModel(
    private val repository: ProjectRepository = ProjectRepository()
) : ViewModel() {

    var uiState by mutableStateOf(CreateProjectUiState())
        private set

    fun setName(name: String) {
        uiState = uiState.copy(name = name)
    }

    fun setInspectorCount(count: Int) {
        uiState = uiState.copy(minInspectors = count)
    }

    fun setPrioritizer(prioritizer: Prioritizer) {
        uiState = uiState.copy(prioritizer = prioritizer)
    }

    fun setTargetDate(date: LocalDate) {
        uiState = uiState.copy(targetDate = date)
    }

    fun createProject() {
        viewModelScope.launch {
            uiState = uiState.copy(loading = true)

            try {
                repository.createProject(
                    name = uiState.name,
                    minInspectors = uiState.minInspectors,
                    prioritizer = uiState.prioritizer,
                    teamId = requireTeam().id,
                    targetDate = uiState.targetDate
                )

                uiState = uiState.copy(created = true)
            } catch (error: Throwable) {
                uiState = uiState.copy(error = true)
            } finally {
                uiState = uiState.copy(loading = false)
            }
        }
    }

    fun isWiegersSumValid(): Boolean {
        val prioritizer = uiState.prioritizer as? WiegersPrioritizer ?: return true
        val totalWeight = prioritizer.userValueWeight + prioritizer.complexityWeight + prioritizer.impactWeight
        return totalWeight == 1.0f
    }

    fun dismissError() {
        uiState = uiState.copy(error = false)
    }
}
