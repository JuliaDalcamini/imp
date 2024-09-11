package com.julia.imp.inspection.create

import com.julia.imp.inspection.answer.AnswerOption

data class QuestionState(
    val answer: AnswerOption? = null,
    val defectDetail: String? = null
)
