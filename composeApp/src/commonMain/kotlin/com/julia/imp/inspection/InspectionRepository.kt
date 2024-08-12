package com.julia.imp.inspection

import com.julia.imp.common.network.configuredHttpClient
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get

class InspectionRepository(
    private val client: HttpClient = configuredHttpClient
) {

    suspend fun getInspections(projectId: String, artifactId: String): List<Inspection> =
        client.get("projects/$projectId/artifacts/$artifactId/inspections").body()
}