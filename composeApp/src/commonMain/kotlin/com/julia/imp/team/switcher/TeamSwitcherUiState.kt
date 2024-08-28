package com.julia.imp.team.switcher

import com.julia.imp.common.session.UserSession
import com.julia.imp.common.session.requireTeam
import com.julia.imp.team.Team

data class TeamSwitcherUiState(
    val switcherOpen: Boolean = false,
    val loading: Boolean = false,
    val teams: List<Team>? = null,
    val error: TeamSwitcherError? = null,
    val newSession: UserSession? = null,
    val showManageOption: Boolean = false
) {

    val currentTeam: Team
        get() = requireTeam()
}