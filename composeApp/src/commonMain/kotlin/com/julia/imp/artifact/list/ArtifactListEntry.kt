package com.julia.imp.artifact.list

import com.julia.imp.artifact.Artifact
import kotlinx.serialization.Serializable

@Serializable
data class ArtifactListEntry(
    val artifact: Artifact,
    val showManagementOptions: Boolean
)