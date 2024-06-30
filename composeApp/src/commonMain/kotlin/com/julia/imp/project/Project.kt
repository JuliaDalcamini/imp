package com.julia.imp.project

import com.julia.imp.priority.Prioritizer
import com.julia.imp.team.Team
import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

@Serializable
data class Project(
    val id: String,
    val name: String,
    val creationDateTime: Instant,
    val creator: ProjectCreator,
    val prioritizer: Prioritizer,
    val team: Team
) {

    @Serializable
    data class ProjectCreator(
        val id: String,
        val firstName: String,
        val lastName: String
    ) {
        val fullName by lazy { "$firstName $lastName" }
    }
}