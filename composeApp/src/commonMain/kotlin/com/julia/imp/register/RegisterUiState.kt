package com.julia.imp.register

import com.julia.imp.common.auth.UserCredentials

data class RegisterUiState(
    val firstName: String = "",
    val lastName: String = "",
    val email: String = "",
    val password: String = "",
    val passwordConfirmation: String = "",
    val passwordMismatch: Boolean = false,
    val showError: Boolean = false,
    val isLoading: Boolean = false,
    val registeredCredentials: UserCredentials? = null
)