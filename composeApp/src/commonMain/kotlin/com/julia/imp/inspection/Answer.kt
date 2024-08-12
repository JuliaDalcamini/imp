package com.julia.imp.inspection

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

// TODO: Move to proper package
@Serializable
enum class Answer {
    @SerialName("na") NotApplicable,
    @SerialName("yes") Yes,
    @SerialName("no") No
}