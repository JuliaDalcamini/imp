package com.julia.imp.team.manage

import kotlinx.serialization.Serializable

@Serializable
data class UpdateTeamRequest(
    val name: String
)