package com.julia.imp.common.session

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

object SessionManager {

    var activeSession: UserSession? by mutableStateOf(null)
}