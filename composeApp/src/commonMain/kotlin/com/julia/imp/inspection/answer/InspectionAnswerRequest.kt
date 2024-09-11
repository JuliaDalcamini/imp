package com.julia.imp.inspection.answer

import kotlinx.serialization.Serializable

@Serializable
data class InspectionAnswerRequest(
    val questionId: String,
    val answer: AnswerOption,
    val defectDetail: String?
)