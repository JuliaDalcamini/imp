package com.julia.imp.login

import com.julia.imp.common.auth.TokenPair
import kotlinx.serialization.Serializable

@Serializable
data class LoginResponse(
    val userId: String,
    val tokens: TokenPair
)
