package com.julia.imp.register

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.julia.imp.common.auth.UserCredentials
import kotlinx.coroutines.launch

class RegisterViewModel(
    private val repository: RegisterRepository = RegisterRepository()
) : ViewModel() {

    var uiState by mutableStateOf(RegisterUiState())
        private set

    fun register() {
        viewModelScope.launch {
            uiState = uiState.copy(showError = false, loading = true)

            try {
                val credentials = UserCredentials(
                    email = uiState.email,
                    password = uiState.password
                )

                repository.register(
                    firstName = uiState.firstName,
                    lastName = uiState.lastName,
                    credentials = credentials
                )

                uiState = uiState.copy(registeredCredentials = credentials)
            } catch (error: Throwable) {
                uiState = uiState.copy(showError = true)
            } finally {
                uiState = uiState.copy(loading = false)
            }
        }
    }

    fun setFirstName(firstName: String) {
        uiState = uiState.copy(firstName = firstName)
    }

    fun setLastName(lastName: String) {
        uiState = uiState.copy(lastName = lastName)
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
