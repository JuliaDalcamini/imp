package com.julia.imp.project.create

import com.julia.imp.priority.Prioritizer
import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable

@Serializable
data class CreateProjectRequest(
    val name: String,
    val startDate: LocalDate,
    val targetDate: LocalDate,
    val minInspectors: Int,
    val prioritizer: Prioritizer,
    val teamId: String
)