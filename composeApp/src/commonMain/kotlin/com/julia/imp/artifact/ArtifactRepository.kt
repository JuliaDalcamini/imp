package com.julia.imp.artifact

import com.julia.imp.artifact.create.CreateArtifactRequest
import com.julia.imp.artifact.edit.UpdateArtifactRequest
import com.julia.imp.artifact.list.ArtifactFilter
import com.julia.imp.common.network.configuredHttpClient
import com.julia.imp.priority.Priority
import com.julia.imp.team.inspector.Inspector
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.patch
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType

class ArtifactRepository(
    private val client: HttpClient = configuredHttpClient
) {
    suspend fun getArtifactTypes(): List<ArtifactType> =
        client.get("artifact-types").body()

    suspend fun getArtifacts(projectId: String, filter: ArtifactFilter): List<Artifact> =
        client.get("projects/$projectId/artifacts?filter=$filter").body()

    suspend fun archiveArtifact(projectId: String, artifactId: String) {
        client.post("projects/$projectId/artifacts/$artifactId/archive")
    }

    suspend fun createArtifact(
        projectId: String,
        name: String,
        type: ArtifactType,
        priority: Priority,
        inspectors: List<Inspector>
    ) {
        client.post("projects/$projectId/artifacts") {
            contentType(ContentType.Application.Json)

            setBody(
                CreateArtifactRequest(
                    name = name,
                    artifactTypeId = type.id,
                    priority = priority,
                    inspectorIds = inspectors.map { it.id }
                )
            )
        }
    }

    suspend fun updateArtifact(artifact: Artifact) {
        client.patch("projects/${artifact.projectId}/artifacts/${artifact.id}") {
            contentType(ContentType.Application.Json)

            setBody(
                UpdateArtifactRequest(
                    name = artifact.name,
                    artifactTypeId = artifact.type.id,
                    priority = artifact.priority,
                    inspectorIds = artifact.inspectors.map { it.id }
                )
            )
        }
    }
}