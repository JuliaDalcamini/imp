package com.julia.imp.inspection.create

import com.julia.imp.inspection.answer.AnswerOption
import kotlinx.serialization.Serializable
import kotlin.time.Duration

@Serializable
data class CreateInspectionRequest(
    val duration: Duration,
    val answers: Map<String, AnswerOption>
)