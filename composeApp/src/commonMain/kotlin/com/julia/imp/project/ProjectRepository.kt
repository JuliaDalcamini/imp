package com.julia.imp.project

import com.julia.imp.common.network.configuredHttpClient
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType

class ProjectRepository(private val client: HttpClient = configuredHttpClient) {
    
    suspend fun getProjects(teamId: String): List<Project> =
        client.get("projects?teamId=$teamId").body()

    suspend fun deleteProject(projectId: String) {
        client.delete("projects/$projectId")
    }
}