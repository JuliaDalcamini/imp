package com.julia.imp.team.inspector

import com.julia.imp.common.network.configuredHttpClient
import com.julia.imp.user.User
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get

class InspectorRepository(private val client: HttpClient = configuredHttpClient) {

    suspend fun getInspectors(teamId: String): List<User> =
        client.get("teams/$teamId/inspectors").body()
}