package com.julia.imp.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.julia.imp.common.network.setAuthTokens
import com.julia.imp.common.session.SessionManager
import com.julia.imp.common.session.UserSession
import com.julia.imp.team.TeamRepository
import kotlinx.coroutines.launch

class LoginViewModel(
    private val repository: LoginRepository = LoginRepository(),
    private val teamRepository: TeamRepository = TeamRepository()
) : ViewModel() {
    
    var uiState by mutableStateOf(LoginUiState())
        private set

    fun clearSession() {
        SessionManager.activeSession = null
    }
    
    fun login() {
        viewModelScope.launch {
            uiState = uiState.copy(showError = false, isLoading = true)
            
            try {
                val response = repository.login(uiState.email, uiState.password)
                
                setAuthTokens(response.tokens)

                val teams = teamRepository.getTeams()
                val initialTeam = teams.first()
                val teamMember = teamRepository.getMember(initialTeam.id, response.userId)

                SessionManager.activeSession = UserSession(
                    userId = response.userId,
                    team = initialTeam,
                    roleInTeam = teamMember.role
                )
            } catch (error: Throwable) {
                uiState = uiState.copy(showError = true)
            } finally {
                uiState = uiState.copy(isLoading = false)
            }
        }
    }
    
    fun setEmail(email: String) {
        uiState = uiState.copy(email = email)
    }
    
    fun setPassword(password: String) {
        uiState = uiState.copy(password = password)
    }

    fun dismissError() {
        uiState = uiState.copy(showError = false)
    }
}