package com.julia.imp.inspection.answer

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class AnswerOption {
    @SerialName("yes") Yes,
    @SerialName("no") No,
    @SerialName("notApplicable") NotApplicable
}