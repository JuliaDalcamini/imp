package com.julia.imp.project.manage

import kotlinx.serialization.Serializable

@Serializable
data class ManageProjectRoute(
    val projectId: String
)