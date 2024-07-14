package com.julia.imp.team.create

import kotlinx.serialization.Serializable

@Serializable
data class CreateTeamRequest(
    val name: String
)