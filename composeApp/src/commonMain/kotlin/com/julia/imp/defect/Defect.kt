package com.julia.imp.defect

import com.julia.imp.artifact.ArtifactReference
import com.julia.imp.question.DefectType
import com.julia.imp.question.Severity
import kotlinx.serialization.Serializable

@Serializable
data class Defect(
    val id: String,
    val type: DefectType,
    val artifact: ArtifactReference,
    val severity: Severity,
    val description: String?,
    val fixed: Boolean
)