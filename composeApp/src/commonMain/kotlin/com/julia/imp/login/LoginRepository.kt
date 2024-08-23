package com.julia.imp.login

import com.julia.imp.common.network.configuredHttpClient
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType

class LoginRepository(private val client: HttpClient = configuredHttpClient) {
    
    suspend fun login(email: String, password: String): LoginResponse {
        val response = client.post("login") {
            contentType(ContentType.Application.Json)
            setBody(LoginRequest(email, password))
        }
        
        return response.body()
    }
}