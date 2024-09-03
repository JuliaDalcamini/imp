package com.julia.imp.project.create

import com.julia.imp.priority.Prioritizer
import kotlinx.serialization.Serializable

@Serializable
data class CreateProjectRequest(
    val name: String,
    val totalInspectors: Int,
    val prioritizer: Prioritizer,
    val teamId: String
)