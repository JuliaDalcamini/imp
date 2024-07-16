package com.julia.imp.team.member

import com.julia.imp.common.network.configuredHttpClient
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.patch
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType

class TeamMemberRepository(private val client: HttpClient = configuredHttpClient) {

    suspend fun getMember(teamId: String, userId: String): TeamMember =
        client.get("teams/$teamId/members/$userId").body()

    suspend fun getMembers(teamId: String): List<TeamMember> =
        client.get("teams/$teamId/members").body()

    suspend fun addMember(teamId: String, email: String, role: Role) {
        client.post("teams/$teamId/members") {
            contentType(ContentType.Application.Json)
            setBody(AddTeamMemberRequest(email = email, role = role))
        }
    }

    suspend fun removeMember(teamId: String, userId: String) {
        client.delete("teams/$teamId/members/$userId")
    }

    suspend fun updateMemberRole(teamId: String, userId: String, newRole: Role) {
        client.patch("teams/$teamId/members/$userId") {
            contentType(ContentType.Application.Json)
            setBody(UpdateTeamMemberRequest(role = newRole))
        }
    }
}