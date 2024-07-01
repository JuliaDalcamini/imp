package com.julia.imp.artifact.list

import com.julia.imp.artifact.ArtifactType
import com.julia.imp.priority.Priority
import kotlinx.serialization.Serializable

@Serializable
data class ArtifactListEntry(
    val id: String,
    val name: String,
    val type: ArtifactType,
    val projectId: String,
    val priority: Priority
)