package com.julia.imp.question

import kotlinx.serialization.Serializable

@Serializable
data class DefectType(
    val id: String,
    val name: String
)
