package com.julia.imp.project.dashboard

import com.julia.imp.common.network.configuredHttpClient
import com.julia.imp.project.dashboard.data.DashboardData
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get

class DashboardRepository(private val client: HttpClient = configuredHttpClient) {
    
    suspend fun getDashboardData(projectId: String): DashboardData =
        client.get("projects/$projectId/dashboard").body()
}