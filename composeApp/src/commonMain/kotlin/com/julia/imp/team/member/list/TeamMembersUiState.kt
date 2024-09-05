package com.julia.imp.team.member.list

import com.julia.imp.team.member.TeamMember

data class TeamMembersUiState(
    val loading: Boolean = false,
    val members: List<TeamMember>? = null,
    val error: Boolean = false,
    val showAddButton: Boolean = false,
    val showOptions: Boolean = false,
    val showAddDialog: Boolean = false,
    val memberToChangeRole: TeamMember? = null,
    val memberToRemove: TeamMember? = null,
    val memberToUpdateHourlyCost: TeamMember? = null,
    val actionError: Boolean = false,
    val showUpdateHourlyCostDialog: Boolean = false
)