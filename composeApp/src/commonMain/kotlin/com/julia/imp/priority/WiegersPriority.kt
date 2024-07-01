package com.julia.imp.priority

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("wiegers")
data class WiegersPriority(
    val userValue: Int,
    val complexity: Int,
    val impact: Int
) : Priority()