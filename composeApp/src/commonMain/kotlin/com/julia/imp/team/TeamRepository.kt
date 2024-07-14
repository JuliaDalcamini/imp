package com.julia.imp.team

import com.julia.imp.common.network.configuredHttpClient
import com.julia.imp.team.create.CreateTeamRequest
import com.julia.imp.team.member.TeamMember
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType

class TeamRepository(private val client: HttpClient = configuredHttpClient) {
    
    suspend fun getTeams(): List<Team> =
        client.get("teams").body()

    suspend fun getMember(teamId: String, userId: String): TeamMember =
        client.get("teams/$teamId/members/$userId").body()

    suspend fun createTeam(name: String): Team =
        client.post("teams") {
            contentType(ContentType.Application.Json)
            setBody(CreateTeamRequest(name = name))
        }.body()
}