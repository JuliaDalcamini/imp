package com.julia.imp.team.create

import com.julia.imp.common.session.UserSession

data class CreateTeamUiState(
    val name: String = "",
    val defaultHourlyCost: Double = 73.23,
    val loading: Boolean = false,
    val error: Boolean = false,
    val newSession: UserSession? = null
)