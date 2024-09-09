package com.julia.imp.project.dashboard.data

import com.julia.imp.artifact.ArtifactType
import kotlinx.serialization.Serializable

@Serializable
data class ArtifactTypeDefectSummary(
    val artifactType: ArtifactType,
    val percentage: Double,
    val total: CountAndCost,
    val lowSeverity: CountAndCost,
    val mediumSeverity: CountAndCost,
    val highSeverity: CountAndCost
)
