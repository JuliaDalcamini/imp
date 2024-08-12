package com.julia.imp.project

import com.julia.imp.priority.Prioritizer
import com.julia.imp.team.Team
import com.julia.imp.user.User
import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

@Serializable
data class Project(
    val id: String,
    val name: String,
    val creationDateTime: Instant,
    val creator: User,
    val prioritizer: Prioritizer,
    val team: Team
)