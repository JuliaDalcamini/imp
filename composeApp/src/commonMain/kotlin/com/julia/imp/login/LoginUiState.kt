package com.julia.imp.login

data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val showError: Boolean = false,
    val isLoading: Boolean = false
)