package com.julia.imp.priority

import androidx.annotation.FloatRange
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("wiegers")
data class WiegersPrioritizer(
    @FloatRange(0.0, 1.0) val userValueWeight: Float = 0.1f,
    @FloatRange(0.0, 1.0) val complexityWeight: Float = 0.1f,
    @FloatRange(0.0, 1.0) val impactWeight: Float = 0.1f
) : Prioritizer()