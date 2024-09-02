package com.julia.imp.artifact.list

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Serializable
enum class ArtifactFilter {
    @SerialName("assignedToMe") AssignedToMe,
    @SerialName("prioritized") Prioritized,
    @SerialName("notPrioritized") NotPrioritized,
    @SerialName("archived") Archived,
    @SerialName("all") All;

    override fun toString(): String = Json.encodeToString(this)
}