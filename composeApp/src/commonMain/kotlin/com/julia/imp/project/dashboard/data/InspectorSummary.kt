package com.julia.imp.project.dashboard.data

import com.julia.imp.user.User
import kotlinx.serialization.Serializable
import kotlin.time.Duration

@Serializable
data class InspectorSummary(
    val inspector: User,
    val totalEffort: Duration,
    val totalCost: Double,
    val progress: Progress
)
