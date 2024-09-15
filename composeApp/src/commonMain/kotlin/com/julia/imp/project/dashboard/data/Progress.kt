package com.julia.imp.project.dashboard.data

import kotlinx.serialization.Serializable

@Serializable
data class Progress(
    val percentage: Double,
    val count: Int,
    val total: Int
)
