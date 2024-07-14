package com.julia.imp.project.create

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.julia.imp.common.session.requireTeam
import com.julia.imp.priority.Prioritizer
import com.julia.imp.project.ProjectRepository
import kotlinx.coroutines.launch

class CreateProjectViewModel(
    private val repository: ProjectRepository = ProjectRepository()
) : ViewModel() {

    var uiState by mutableStateOf(CreateProjectUiState())
        private set

    fun setName(name: String) {
        uiState = uiState.copy(name = name)
    }

    fun setPrioritizer(prioritizer: Prioritizer) {
        uiState = uiState.copy(prioritizer = prioritizer)
    }

    fun createProject() {
        viewModelScope.launch {
            uiState = uiState.copy(loading = true)

            try {
                repository.createProject(
                    name = uiState.name,
                    prioritizer = uiState.prioritizer,
                    teamId = requireTeam().id
                )

                uiState = uiState.copy(created = true)
            } catch (error: Throwable) {
                // TODO: Handle error
            } finally {
                uiState = uiState.copy(loading = false)
            }
        }
    }
}
