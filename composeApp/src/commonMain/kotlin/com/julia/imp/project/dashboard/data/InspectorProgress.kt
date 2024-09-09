package com.julia.imp.project.dashboard.data

import com.julia.imp.user.User
import kotlinx.serialization.Serializable

@Serializable
data class InspectorProgress(
    val inspector: User,
    val percentage: Double,
    val count: Int,
    val total: Int
)
