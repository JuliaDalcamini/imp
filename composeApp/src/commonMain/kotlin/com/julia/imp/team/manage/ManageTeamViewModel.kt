package com.julia.imp.team.manage

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.julia.imp.team.TeamRepository

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
}
