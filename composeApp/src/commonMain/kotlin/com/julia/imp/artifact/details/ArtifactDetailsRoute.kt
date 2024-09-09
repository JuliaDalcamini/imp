package com.julia.imp.artifact.details

import com.julia.imp.artifact.Artifact
import kotlinx.serialization.Serializable

@Serializable
data class ArtifactDetailsRoute(
    val artifactId: String,
    val projectId: String
) {

    companion object {

        fun of(artifact: Artifact): ArtifactDetailsRoute =
            ArtifactDetailsRoute(
                artifactId = artifact.id,
                projectId = artifact.projectId
            )
    }
}