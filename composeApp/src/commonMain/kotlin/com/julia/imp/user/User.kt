package com.julia.imp.user

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val id: String,
    val firstName: String,
    val lastName: String
) {
    val fullName by lazy { "$firstName $lastName" }
}