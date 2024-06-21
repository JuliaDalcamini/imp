package com.julia.imp.priority

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("wiegers")
data class WiegersPrioritizer(
    val userValueWeight: Double,
    val complexityWeight: Double,
    val impactWeight: Double
) : Prioritizer()