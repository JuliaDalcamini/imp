package com.julia.imp.question

import com.julia.imp.common.network.configuredHttpClient
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get

class QuestionRepository(private val client: HttpClient = configuredHttpClient) {
    
    suspend fun getQuestions(artifactTypeId: String): List<Question> =
        client.get("questions?artifactTypeId=$artifactTypeId").body()
}