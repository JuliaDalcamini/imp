package com.julia.imp.login

import com.julia.imp.team.Team

data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val showError: Boolean = false,
    val isLoggingIn: Boolean = false,
    val loggedTeam: Team? = null
)