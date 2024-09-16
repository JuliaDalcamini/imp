package com.julia.imp.project.dashboard.data

import com.julia.imp.artifact.ArtifactType
import kotlinx.serialization.Serializable
import kotlin.time.Duration

@Serializable
data class ArtifactTypeSummary(
    val artifactType: ArtifactType,
    val totalEffort: Duration,
    val totalCost: Double,
    val defects: ArtifactTypeDefects
)
