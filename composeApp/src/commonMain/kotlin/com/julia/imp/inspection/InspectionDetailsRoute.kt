package com.julia.imp.inspection

import com.julia.imp.artifact.Artifact
import kotlinx.serialization.Serializable

@Serializable
data class InspectionDetailsRoute(
    val artifactId: String,
    val projectId: String,
    val inspectionId: String
) {

    companion object {

        fun of(artifact: Artifact, inspection: Inspection): InspectionDetailsRoute =
            InspectionDetailsRoute(
                artifactId = artifact.id,
                projectId = artifact.projectId,
                inspectionId = inspection.id
            )
    }
}