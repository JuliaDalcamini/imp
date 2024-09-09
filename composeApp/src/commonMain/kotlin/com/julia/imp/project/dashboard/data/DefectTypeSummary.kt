package com.julia.imp.project.dashboard.data

import com.julia.imp.question.DefectType
import kotlinx.serialization.Serializable
import kotlin.time.Duration

@Serializable
data class DefectTypeSummary(
    val defectType: DefectType,
    val percentage: Double,
    val count: Int,
    val averageCost: Double,
    val averageEffort: Duration
)
