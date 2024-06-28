package com.julia.imp.project

import kotlinx.serialization.Serializable

@Serializable
data class UpdateProjectRequest(
    val name: String
)