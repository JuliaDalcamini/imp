package com.julia.imp.question

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class Severity {
    @SerialName("low") Low,
    @SerialName("medium") Medium,
    @SerialName("high") High
}