package com.julia.imp.logout

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class LogoutViewModel : ViewModel() {
    
    var uiState by mutableStateOf(LogoutUiState())
        private set
    
    fun logout() {
        viewModelScope.launch {
            delay(1000)
            uiState = uiState.copy(loggedOut = true)
        }
    }
}