package com.julia.imp.team.manage

import com.julia.imp.team.Team

data class ManageTeamUiState(
    val showRenameDialog: Boolean = false,
    val showUpdateDefaultCostDialog: Boolean = false,
    val actionError: Boolean = false,
    val updatedTeam: Team? = null,
    val error: Boolean = false
)
