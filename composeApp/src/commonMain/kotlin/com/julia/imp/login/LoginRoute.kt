package com.julia.imp.login

import com.julia.imp.common.auth.UserCredentials
import kotlinx.serialization.Serializable

@Serializable
data class LoginRoute(
    val email: String? = null,
    val password: String? = null
) {

    companion object {

        fun of(credentials: UserCredentials?) =
            LoginRoute(
                email = credentials?.email,
                password = credentials?.password
            )
    }
}