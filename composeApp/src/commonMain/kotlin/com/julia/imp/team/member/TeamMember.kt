package com.julia.imp.team.member

import kotlinx.serialization.Serializable

@Serializable
data class TeamMember(
    val userId: String,
    val teamId: String,
    val role: Role
)