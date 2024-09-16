package com.julia.imp.project.dashboard.data

import kotlinx.serialization.Serializable

@Serializable
data class ArtifactTypeDefects(
    val percentage: Double,
    val total: CountAndCost,
    val lowSeverity: CountAndCost,
    val mediumSeverity: CountAndCost,
    val highSeverity: CountAndCost
)
