package com.julia.imp.team

import kotlinx.serialization.Serializable

@Serializable
data class Team(
    val id: String,
    val name: String,
    val defaultHourlyCost: Double
)