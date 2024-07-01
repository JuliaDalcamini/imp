package com.julia.imp.artifact

import com.julia.imp.artifact.list.ArtifactListEntry
import com.julia.imp.common.network.configuredHttpClient
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get

class ArtifactRepository(
    private val client: HttpClient = configuredHttpClient
) {

    suspend fun getArtifacts(projectId: String): List<ArtifactListEntry> =
        client.get("/projects/$projectId/artifacts").body()
}