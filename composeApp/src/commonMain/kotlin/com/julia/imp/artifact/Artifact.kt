package com.julia.imp.artifact

import com.julia.imp.priority.Priority
import kotlinx.serialization.Serializable

@Serializable
data class Artifact(
    val id: String,
    val name: String,
    val type: ArtifactType,
    val projectId: String,
    val priority: Priority,
    val archived: Boolean
)