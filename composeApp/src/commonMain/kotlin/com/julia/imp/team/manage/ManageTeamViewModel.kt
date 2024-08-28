package com.julia.imp.team.manage

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.julia.imp.common.session.requireSession
import com.julia.imp.common.session.requireTeam
import com.julia.imp.project.list.ProjectsUiState
import com.julia.imp.team.Team
import com.julia.imp.team.TeamRepository
import kotlinx.coroutines.launch

class ManageTeamViewModel(
    private val repository: TeamRepository = TeamRepository()
) : ViewModel() {

    var uiState by mutableStateOf(ManageTeamUiState())
        private set

    fun showRenameDialog() {
        uiState = uiState.copy(showRenameDialog = true)
    }

    fun dismissRenameDialog() {
        uiState = uiState.copy(showRenameDialog = false)
    }

    fun renameTeam(team: Team, newName: String) {
        viewModelScope.launch {
            try {
                val updatedTeam = repository.renameTeam(team.id, newName)
                uiState = uiState.copy(updatedTeam = updatedTeam)
            } catch (error: Throwable) {
                uiState = uiState.copy(actionError = true)
            }
        }
    }
}
