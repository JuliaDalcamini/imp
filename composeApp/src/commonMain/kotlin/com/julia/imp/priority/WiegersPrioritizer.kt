package com.julia.imp.priority

import androidx.annotation.FloatRange
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("wiegers")
data class WiegersPrioritizer(
    @FloatRange(1.0, 5.0) val userValueWeight: Double = 3.0,
    @FloatRange(1.0, 5.0) val complexityWeight: Double = 3.0,
    @FloatRange(1.0, 5.0) val impactWeight: Double = 3.0
) : Prioritizer()