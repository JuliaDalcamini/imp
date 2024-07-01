package com.julia.imp.priority

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class MoscowPriorityLevel {
    @SerialName("wontHave") WontHave,
    @SerialName("couldHave") CouldHave,
    @SerialName("shouldHave") ShouldHave,
    @SerialName("mustHave") MustHave
}