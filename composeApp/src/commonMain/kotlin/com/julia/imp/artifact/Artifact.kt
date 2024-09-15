package com.julia.imp.artifact

import com.julia.imp.priority.Priority
import com.julia.imp.user.User
import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

@Serializable
data class Artifact(
    val id: String,
    val name: String,
    val externalLink: String,
    val type: ArtifactType,
    val projectId: String,
    val priority: Priority?,
    val archived: Boolean,
    val inspectors: List<User>,
    val calculatedPriority: Double?,
    val lastModification: Instant,
    val currentVersion: String,
    val totalCost: Double,
    val fullyInspected: Boolean
)