package com.julia.imp.defect

import com.julia.imp.artifact.Artifact
import com.julia.imp.common.network.configuredHttpClient
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.patch
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType

class DefectRepository(private val client: HttpClient = configuredHttpClient) {

    suspend fun getDefects(projectId: String, artifactId: String, filter: DefectFilter): List<Defect> =
        client.get("projects/$projectId/artifacts/$artifactId/defects?filter=$filter").body()

    suspend fun updateDefect(artifact: Artifact, defectId: String, fixed: Boolean): Defect =
        client
            .patch(
                "projects/${artifact.projectId}/artifacts/${artifact.id}/defects/$defectId"
            ) {
                contentType(ContentType.Application.Json)

                setBody(
                    UpdateDefectRequest(fixed = fixed)
                )
            }.body()
}