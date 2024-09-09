package com.julia.imp.project.dashboard.data

import kotlinx.serialization.Serializable

@Serializable
data class CountAndCost(
    val count: Int,
    val cost: Double
)
