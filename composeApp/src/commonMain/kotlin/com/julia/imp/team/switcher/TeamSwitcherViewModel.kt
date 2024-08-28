package com.julia.imp.team.switcher

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.julia.imp.common.session.UserSession
import com.julia.imp.common.session.requireSession
import com.julia.imp.team.Team
import com.julia.imp.team.TeamRepository
import com.julia.imp.team.manage.ManageTeamUiState
import com.julia.imp.team.member.TeamMember
import com.julia.imp.team.member.TeamMemberRepository
import com.julia.imp.team.switcher.TeamSwitcherError.ErrorLoadingTeams
import com.julia.imp.team.switcher.TeamSwitcherError.ErrorSwitchingActiveTeam
import kotlinx.coroutines.launch

class TeamSwitcherViewModel(
    private val repository: TeamRepository = TeamRepository(),
    private val memberRepository: TeamMemberRepository = TeamMemberRepository()
) : ViewModel() {

    var uiState by mutableStateOf(TeamSwitcherUiState())
        private set

    fun openSwitcher() {
        uiState = uiState.copy(
            loading = true,
            teams = null,
            error = null,
            switcherOpen = true
        )

        viewModelScope.launch {
            try {
                val isAdmin = requireSession().isTeamAdmin

                uiState = uiState.copy(
                    showManageOption = isAdmin
                )

                val teams = repository.getTeams()
                uiState = uiState.copy(teams = teams)
            } catch (error: Throwable) {
                uiState = uiState.copy(error = ErrorLoadingTeams)
            } finally {
                uiState = uiState.copy(loading = false)
            }
        }
    }

    fun closeSwitcher() {
        uiState = uiState.copy(switcherOpen = false)
    }

    fun switchToTeam(team: Team) {
        uiState = uiState.copy(loading = true, teams = null)

        viewModelScope.launch {
            try {
                val member = memberRepository.getMember(
                    teamId = team.id,
                    userId = requireSession().userId
                )

                uiState = uiState.copy(newSession = createUserSession(team, member))
                closeSwitcher()
            } catch (error: Throwable) {
                uiState = uiState.copy(error = ErrorSwitchingActiveTeam)
            } finally {
                uiState = uiState.copy(loading = false)
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