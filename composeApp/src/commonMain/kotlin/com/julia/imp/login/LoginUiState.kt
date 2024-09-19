package com.julia.imp.login

import com.julia.imp.common.session.UserSession

data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val showError: Boolean = false,
    val loading: Boolean = false,
    val newSession: UserSession? = null
)