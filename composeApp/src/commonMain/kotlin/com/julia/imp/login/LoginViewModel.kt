package com.julia.imp.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.julia.imp.common.network.setAuthTokens
import com.julia.imp.team.TeamRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoginViewModel(
    private val repository: LoginRepository = LoginRepository(),
    private val teamRepository: TeamRepository = TeamRepository()
) : ViewModel() {
    
    private val mutableUiState = MutableStateFlow(LoginUiState())
    val uiState = mutableUiState.asStateFlow()
    
    fun login() {
        viewModelScope.launch {
            mutableUiState.update { it.copy(showError = false, isLoggingIn = true) }
            
            try {
                val authTokens = repository.login(
                    email = uiState.value.email,
                    password = uiState.value.password
                )
                
                setAuthTokens(authTokens)

                val teams = teamRepository.getTeams()

                mutableUiState.update {
                    it.copy(loggedTeam = teams.first())
                }
            } catch (error: Throwable) {
                mutableUiState.update { it.copy(showError = true) }
            } finally {
                mutableUiState.update { it.copy(isLoggingIn = false) }
            }
        }
    }
    
    fun setEmail(email: String) {
        mutableUiState.update { it.copy(email = email) }
    }
    
    fun setPassword(password: String) {
        mutableUiState.update { it.copy(password = password) }
    }

    fun dismissError() {
        mutableUiState.update { it.copy(showError = false) }
    }
}