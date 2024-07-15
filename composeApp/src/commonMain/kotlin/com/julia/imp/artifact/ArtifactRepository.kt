package com.julia.imp.artifact

import com.julia.imp.artifact.list.ArtifactFilter
import com.julia.imp.common.network.configuredHttpClient
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post

class ArtifactRepository(
    private val client: HttpClient = configuredHttpClient
) {

    suspend fun getArtifacts(projectId: String, filter: ArtifactFilter): List<Artifact> =
        client.get("/projects/$projectId/artifacts?filter=$filter").body()

    suspend fun archiveArtifact(projectId: String, artifactId: String) {
        client.post("projects/$projectId/artifacts/$artifactId/archive")
    }
}