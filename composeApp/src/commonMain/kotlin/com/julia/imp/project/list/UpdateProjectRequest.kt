package com.julia.imp.project.list

import kotlinx.serialization.Serializable

@Serializable
data class UpdateProjectRequest(
    val name: String
)