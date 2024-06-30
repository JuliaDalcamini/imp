package com.julia.imp.register

data class RegisterUiState(
    val email: String = "",
    val password: String = "",
    val passwordConfirmation: String = "",
    val passwordMismatch: Boolean = false,
    val showError: Boolean = false,
    val isLoading: Boolean = false
)