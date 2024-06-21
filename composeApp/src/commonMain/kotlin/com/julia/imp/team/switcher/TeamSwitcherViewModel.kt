package com.julia.imp.team.switcher

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.julia.imp.common.network.setAuthTokens
import com.julia.imp.team.Team
import com.julia.imp.team.TeamRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class TeamSwitcherViewModel(
    private val repository: TeamRepository = TeamRepository()
) : ViewModel() {
    
    private val mutableUiState = MutableStateFlow(TeamSwitcherUiState())
    val uiState = mutableUiState.asStateFlow()

    fun openSwitcher() {
        mutableUiState.update {
            it.copy(
                isSwitcherOpen = true,
                isLoading = true,
                teams = null
            )
        }

        viewModelScope.launch {
            try {
                val teams = repository.getTeams()

                mutableUiState.update {
                    it.copy(isLoading = false, teams = teams)
                }
            } catch (error: Throwable) {
                mutableUiState.update { it.copy(error = error.message) }
            } finally {
                mutableUiState.update { it.copy(isLoading = false) }
            }
        }
    }

    fun closeSwitcher() {
        mutableUiState.update {
            it.copy(
                isSwitcherOpen = false,
                isLoading = false,
                teams = null
            )
        }
    }
}