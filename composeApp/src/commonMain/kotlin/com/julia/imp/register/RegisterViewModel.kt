package com.julia.imp.register

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class RegisterViewModel : ViewModel() {

    var uiState by mutableStateOf(RegisterUiState())
        private set

    fun register() {
        // TODO: Implement registration
    }

    fun setEmail(email: String) {
        uiState = uiState.copy(email = email)
    }

    fun setPassword(password: String) {
        uiState = uiState.copy(password = password)
        validatePasswords()
    }

    fun setPasswordConfirmation(passwordConfirmation: String) {
        uiState = uiState.copy(passwordConfirmation = passwordConfirmation)
        validatePasswords()
    }

    fun dismissError() {
        uiState = uiState.copy(showError = false)
    }

    private fun validatePasswords() {
        val mismatch = uiState.passwordConfirmation.isNotEmpty() &&
                uiState.password.isNotEmpty() &&
                uiState.password != uiState.passwordConfirmation

        uiState = uiState.copy(passwordMismatch = mismatch)
    }
}
