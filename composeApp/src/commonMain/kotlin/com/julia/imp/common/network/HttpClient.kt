package com.julia.imp.common.network

import com.julia.imp.common.auth.RefreshTokensRequest
import com.julia.imp.common.auth.TokenPair
import co.touchlab.kermit.Logger as KermitLogger
import io.ktor.client.*
import io.ktor.client.call.body
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.auth.*
import io.ktor.client.plugins.auth.providers.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.plugin
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

private const val API_BASE_URL = "http://192.168.15.22:8080/"
private var authTokens: TokenPair? = null

/**
 * Get an HttpClient suitable for the current platform.
 */
expect fun httpClient(config: HttpClientConfig<*>.() -> Unit = {}): HttpClient

/**
 * Get an already configured HttpClient.
 */
val configuredHttpClient: HttpClient by lazy {
    httpClient {
        install(DefaultRequest) {
            url(API_BASE_URL)
        }

        install(ContentNegotiation) {
            json(
                Json {
                    prettyPrint = true
                    isLenient = true
                    encodeDefaults = true
                }
            )
        }
        
        install(Auth) {
            bearer {
                loadTokens {
                    authTokens?.toBearerTokens()
                }

                refreshTokens {
                    oldTokens?.let {
                        authTokens = client.post("refresh_tokens") {
                            markAsRefreshTokenRequest()
                            contentType(ContentType.Application.Json)
                            setBody(RefreshTokensRequest(it.refreshToken))
                        }.body<TokenPair>()

                        authTokens?.toBearerTokens()
                    }
                }
            }
        }

        install(Logging) {
            logger = createLogger()
            level = LogLevel.BODY
        }
    }
}

private fun invalidateBearerTokens() {
    configuredHttpClient.authProviders
        .filterIsInstance<BearerAuthProvider>()
        .singleOrNull()?.clearToken()
}

/**
 * Clear the auth tokens.
 */
fun clearAuthTokens() {
    authTokens = null
    invalidateBearerTokens()
}

/**
 * Set the auth tokens.
 */
fun setAuthTokens(tokens: TokenPair) {
    authTokens = tokens
    invalidateBearerTokens()
}

private fun createLogger(): Logger {
    val logger = KermitLogger.withTag("HttpClient")

    return object: Logger {
        override fun log(message: String) {
            logger.v(message)
        }
    }
}