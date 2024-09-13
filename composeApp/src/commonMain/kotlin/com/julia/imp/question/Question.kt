package com.julia.imp.question

import kotlinx.serialization.Serializable

@Serializable
data class Question(
    val id: String,
    val text: String
)