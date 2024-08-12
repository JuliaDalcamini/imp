package com.julia.imp.priority

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("wiegers")
data class WiegersPriority(
    val userValue: Int = 1,
    val complexity: Int = 1,
    val impact: Int = 1
) : Priority()