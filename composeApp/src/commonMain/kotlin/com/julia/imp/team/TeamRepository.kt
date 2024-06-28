package com.julia.imp.team

import com.julia.imp.common.network.configuredHttpClient
import com.julia.imp.team.member.TeamMember
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get

class TeamRepository(private val client: HttpClient = configuredHttpClient) {
    
    suspend fun getTeams(): List<Team> =
        client.get("teams").body()

    suspend fun getMember(teamId: String, userId: String): TeamMember =
        client.get("teams/$teamId/members/$userId").body()
}