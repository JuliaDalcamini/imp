package com.julia.imp.team.inspector

import kotlinx.serialization.Serializable

@Serializable
data class Inspector(
    val id: String,
    val firstName: String,
    val lastName: String
) {

    val fullName by lazy { "$firstName $lastName" }
}