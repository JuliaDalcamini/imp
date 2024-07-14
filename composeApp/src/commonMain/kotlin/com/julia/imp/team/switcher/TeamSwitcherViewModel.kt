package com.julia.imp.team.switcher

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.julia.imp.common.session.UserSession
import com.julia.imp.common.session.requireSession
import com.julia.imp.common.session.requireTeam
import com.julia.imp.team.Team
import com.julia.imp.team.TeamRepository
import com.julia.imp.team.member.TeamMember
import com.julia.imp.team.switcher.TeamSwitcherError.ErrorLoadingTeams
import com.julia.imp.team.switcher.TeamSwitcherError.ErrorSwitchingActiveTeam
import kotlinx.coroutines.launch

class TeamSwitcherViewModel(
    private val repository: TeamRepository = TeamRepository()
) : ViewModel() {

    var uiState by mutableStateOf(TeamSwitcherUiState(requireTeam()))
        private set

    fun openSwitcher() {
        uiState = uiState.copy(
            isLoading = true,
            teams = null,
            error = null,
            isSwitcherOpen = true
        )

        viewModelScope.launch {
            try {
                val teams = repository.getTeams()
                uiState = uiState.copy(teams = teams)
            } catch (error: Throwable) {
                uiState = uiState.copy(error = ErrorLoadingTeams)
            } finally {
                uiState = uiState.copy(isLoading = false)
            }
        }
    }

    fun closeSwitcher() {
        uiState = uiState.copy(isSwitcherOpen = false)
    }

    fun switchToTeam(team: Team) {
        uiState = uiState.copy(isLoading = true, teams = null)

        viewModelScope.launch {
            try {
                val member = repository.getMember(
                    teamId = team.id,
                    userId = requireSession().userId
                )

                uiState = uiState.copy(newSession = createUserSession(team, member))
                closeSwitcher()
            } catch (error: Throwable) {
                uiState = uiState.copy(error = ErrorSwitchingActiveTeam)
            } finally {
                uiState = uiState.copy(isLoading = false)
            }
        }
    }

    private fun createUserSession(team: Team, member: TeamMember): UserSession =
        UserSession(
            userId = member.userId,
            team = team,
            roleInTeam = member.role
        )
}