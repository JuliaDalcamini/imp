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

class ProjectRepository(private val client: HttpClient = configuredHttpClient) {
    
    suspend fun getProjects(teamId: String): List<Project> =
        client.get("projects?teamId=$teamId").body()

    suspend fun deleteProject(projectId: String) {
        client.delete("projects/$projectId")
    }

    suspend fun renameProject(projectId: String, newName: String) {
        client.patch("projects/$projectId") {
            contentType(ContentType.Application.Json)
            setBody(UpdateProjectRequest(name = newName))
        }
    }

    suspend fun createProject(name: String, prioritizer: Prioritizer, teamId: String) {
        client.post("projects") {
            contentType(ContentType.Application.Json)

            setBody(
                CreateProjectRequest(
                    name = name,
                    prioritizer = prioritizer,
                    teamId = teamId
                )
            )
        }
    }
}