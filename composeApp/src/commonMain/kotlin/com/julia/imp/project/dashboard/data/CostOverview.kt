package com.julia.imp.project.dashboard.data

import kotlinx.serialization.Serializable

@Serializable
data class CostOverview(
    val total: Double,
    val averagePerArtifact: Double,
    val averagePerInspection: Double
)
