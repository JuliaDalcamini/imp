package com.julia.imp.common.auth

import kotlinx.serialization.Serializable

@Serializable
data class RefreshTokensRequest(
    val refreshToken: String
)