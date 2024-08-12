package com.julia.imp.inspection

import kotlinx.serialization.Serializable

@Serializable
data class InspectionAnswer(
    val id: String,
    val question: Question,
    val answer: Answer
)