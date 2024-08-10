package com.julia.imp.priority

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("wiegers")
data class WiegersPriority(
    val userValue: Int = 0,
    val complexity: Int = 0,
    val impact: Int = 0
) : Priority()