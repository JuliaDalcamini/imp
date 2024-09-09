package com.julia.imp.team.create

import com.julia.imp.common.session.UserSession

data class CreateTeamUiState(
    val name: String = "",
    val loading: Boolean = false,
    val error: Boolean = false,
    val newSession: UserSession? = null
)