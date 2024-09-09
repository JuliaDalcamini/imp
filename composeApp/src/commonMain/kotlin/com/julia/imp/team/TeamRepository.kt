package com.julia.imp.team

import com.julia.imp.common.network.configuredHttpClient
import com.julia.imp.team.create.CreateTeamRequest
import com.julia.imp.team.manage.UpdateTeamRequest
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.patch
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType

class TeamRepository(private val client: HttpClient = configuredHttpClient) {

    suspend fun getTeams(): List<Team> =
        client.get("teams").body()

    suspend fun createTeam(name: String): Team =
        client.post("teams") {
            contentType(ContentType.Application.Json)
            setBody(CreateTeamRequest(name = name))
        }.body()

    suspend fun updateTeam(
        teamId: String,
        newName: String,
        newDefaultHourlyCost: Double
    ): Team =
        client.patch("teams/$teamId") {
            contentType(ContentType.Application.Json)
            setBody(
                UpdateTeamRequest(
                    name = newName,
                    defaultHourlyCost = newDefaultHourlyCost
                )
            )
        }.body()
}