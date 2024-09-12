package com.julia.imp.project

import com.julia.imp.common.network.configuredHttpClient
import com.julia.imp.priority.Prioritizer
import com.julia.imp.project.create.CreateProjectRequest
import com.julia.imp.project.manage.UpdateProjectRequest
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.patch
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.datetime.LocalDate

class ProjectRepository(private val client: HttpClient = configuredHttpClient) {

    suspend fun getProject(projectId: String): Project =
        client.get("projects/$projectId").body()

    suspend fun getProjects(teamId: String, filter: ProjectFilter): List<Project> =
        client.get("projects?teamId=$teamId&filter=$filter").body()

    suspend fun deleteProject(projectId: String) {
        client.delete("projects/$projectId")
    }

    suspend fun updateProject(
        projectId: String,
        name: String,
        startDate: LocalDate,
        targetDate: LocalDate,
        minInspectors: Int,
        finished: Boolean
    ): Project =
        client.patch("projects/$projectId") {
            contentType(ContentType.Application.Json)

            setBody(
                UpdateProjectRequest(
                    name = name,
                    startDate = startDate,
                    targetDate = targetDate,
                    minInspectors = minInspectors,
                    finished = finished
                )
            )
        }.body()

    suspend fun createProject(
        name: String,
        startDate: LocalDate,
        prioritizer: Prioritizer,
        minInspectors: Int,
        teamId: String,
        targetDate: LocalDate
    ) {
        client.post("projects") {
            contentType(ContentType.Application.Json)

            setBody(
                CreateProjectRequest(
                    name = name,
                    startDate = startDate,
                    minInspectors = minInspectors,
                    prioritizer = prioritizer,
                    teamId = teamId,
                    targetDate = targetDate
                )
            )
        }
    }
}