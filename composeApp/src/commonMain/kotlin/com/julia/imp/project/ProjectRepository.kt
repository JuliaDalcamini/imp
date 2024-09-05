package com.julia.imp.project

import com.julia.imp.common.network.configuredHttpClient
import com.julia.imp.priority.Prioritizer
import com.julia.imp.project.create.CreateProjectRequest
import com.julia.imp.project.list.UpdateProjectRequest
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
    
    suspend fun getProjects(teamId: String): List<Project> =
        client.get("projects?teamId=$teamId").body()

    suspend fun deleteProject(projectId: String) {
        client.delete("projects/$projectId")
    }

    suspend fun updateProject(projectId: String, newName: String, newTargetDate: LocalDate, newMinInspectors: Int) {
        client.patch("projects/$projectId") {
            contentType(ContentType.Application.Json)
            setBody(UpdateProjectRequest(name = newName, targetDate = newTargetDate, minInspectors = newMinInspectors))
        }
    }

    suspend fun createProject(name: String, prioritizer: Prioritizer, minInspectors: Int, teamId: String, targetDate: LocalDate) {
        client.post("projects") {
            contentType(ContentType.Application.Json)

            setBody(
                CreateProjectRequest(
                    name = name,
                    minInspectors = minInspectors,
                    prioritizer = prioritizer,
                    teamId = teamId,
                    targetDate = targetDate
                )
            )
        }
    }
}