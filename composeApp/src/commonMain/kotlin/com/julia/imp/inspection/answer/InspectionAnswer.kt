package com.julia.imp.inspection.answer

import com.julia.imp.question.Question
import kotlinx.serialization.Serializable

@Serializable
data class InspectionAnswer(
    val id: String,
    val question: Question,
    val answer: AnswerOption
)