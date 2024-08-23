package com.julia.imp.team.create

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.julia.imp.common.session.UserSession
import com.julia.imp.common.session.requireSession
import com.julia.imp.team.TeamRepository
import com.julia.imp.team.member.TeamMemberRepository
import kotlinx.coroutines.launch

class CreateTeamViewModel(
    private val repository: TeamRepository = TeamRepository(),
    private val memberRepository: TeamMemberRepository = TeamMemberRepository()
) : ViewModel() {

    var uiState by mutableStateOf(CreateTeamUiState())
        private set

    fun setName(name: String) {
        uiState = uiState.copy(name = name)
    }

    fun createTeam() {
        viewModelScope.launch {
            uiState = uiState.copy(loading = true)

            try {
                val team = repository.createTeam(name = uiState.name)
                val userId = requireSession().userId
                val teamMember = memberRepository.getMember(team.id, userId)

                uiState = uiState.copy(
                    newSession = UserSession(
                        userId = userId,
                        team = team,
                        roleInTeam = teamMember.role
                    )
                )
            } catch (error: Throwable) {
                uiState = uiState.copy(error = true)
            } finally {
                uiState = uiState.copy(loading = false)
            }
        }
    }

    fun dismissError() {
        uiState = uiState.copy(error = false)
    }
}
