package com.julia.imp.artifact.edit

import com.julia.imp.priority.Priority
import kotlinx.serialization.Serializable

@Serializable
data class UpdateArtifactRequest(
    val name: String,
    val externalLink: String,
    val artifactTypeId: String,
    val priority: Priority?,
    val inspectorIds: List<String>? = null
)