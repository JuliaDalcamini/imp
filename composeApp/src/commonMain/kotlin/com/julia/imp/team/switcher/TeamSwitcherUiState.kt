package com.julia.imp.team.switcher

import com.julia.imp.team.Team

data class TeamSwitcherUiState(
    val isSwitcherOpen: Boolean = false,
    val isLoading: Boolean = false,
    val teams: List<Team>? = null,
    val error: String? = null
)