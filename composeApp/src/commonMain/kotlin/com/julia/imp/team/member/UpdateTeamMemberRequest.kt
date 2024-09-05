package com.julia.imp.team.member

import kotlinx.serialization.Serializable

@Serializable
data class UpdateTeamMemberRequest(
    val role: Role,
    val hourlyCost: Double
)
