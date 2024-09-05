package com.julia.imp.project.list

import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable

@Serializable
data class UpdateProjectRequest(
    val name: String,
    val targetDate: LocalDate,
    val minInspectors: Int
)