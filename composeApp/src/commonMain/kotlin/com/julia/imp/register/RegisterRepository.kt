package com.julia.imp.register

import com.julia.imp.common.auth.UserCredentials
import com.julia.imp.common.network.configuredHttpClient
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType

class RegisterRepository(private val client: HttpClient = configuredHttpClient) {
    
    suspend fun register(
        firstName: String,
        lastName: String,
        credentials: UserCredentials
    ) {
        val response = client.post("register") {
            contentType(ContentType.Application.Json)

            setBody(
                RegisterRequest(
                    firstName = firstName,
                    lastName = lastName,
                    email = credentials.email,
                    password = credentials.password
                )
            )
        }
        
        return response.body()
    }
}