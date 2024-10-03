package com.julia.imp.project.dashboard.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class PerformanceScore {
    @SerialName("a") A,
    @SerialName("b") B,
    @SerialName("c") C,
    @SerialName("d") D
}