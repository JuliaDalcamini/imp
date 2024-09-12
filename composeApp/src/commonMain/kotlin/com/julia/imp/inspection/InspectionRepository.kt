package com.julia.imp.inspection

import com.julia.imp.common.network.configuredHttpClient
import com.julia.imp.inspection.answer.InspectionAnswerRequest
import com.julia.imp.inspection.create.CreateInspectionRequest
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlin.time.Duration

class InspectionRepository(
    private val client: HttpClient = configuredHttpClient
) {

    suspend fun getInspections(projectId: String, artifactId: String): List<Inspection> =
        client.get("projects/$projectId/artifacts/$artifactId/inspections").body()

    suspend fun getInspection(projectId: String, artifactId: String, inspectionId: String): Inspection =
        client.get("projects/$projectId/artifacts/$artifactId/inspections/$inspectionId").body()

    suspend fun createInspection(
        projectId: String,
        artifactId: String,
        duration: Duration,
        answers: List<InspectionAnswerRequest>
    ): Inspection =
        client.post("projects/$projectId/artifacts/$artifactId/inspections") {
            contentType(ContentType.Application.Json)

            setBody(
                CreateInspectionRequest(
                    duration = duration,
                    answers = answers
                )
            )
        }.body()

}