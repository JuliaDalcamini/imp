package com.julia.imp.common.session

import com.julia.imp.team.Team
import com.julia.imp.team.member.Role

data class UserSession(
    val userId: String,
    val team: Team,
    val roleInTeam: Role
) {

    val isTeamAdmin: Boolean by lazy { roleInTeam == Role.Admin }
}
