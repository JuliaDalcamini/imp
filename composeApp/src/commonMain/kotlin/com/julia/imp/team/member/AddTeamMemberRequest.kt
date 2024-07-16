package com.julia.imp.team.member

import kotlinx.serialization.Serializable

@Serializable
data class AddTeamMemberRequest(
    val email: String,
    val role: Role
)