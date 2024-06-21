package com.julia.imp.common.auth

import io.ktor.client.plugins.auth.providers.BearerTokens
import kotlinx.serialization.Serializable

@Serializable
data class TokenPair(
    val accessToken: String,
    val refreshToken: String
) {

    fun toBearerTokens(): BearerTokens =
        BearerTokens(accessToken, refreshToken)
}