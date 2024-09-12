package com.julia.imp.project.manage

import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable

@Serializable
data class UpdateProjectRequest(
    val name: String,
    val startDate: LocalDate,
    val targetDate: LocalDate,
    val minInspectors: Int,
    val finished: Boolean
)