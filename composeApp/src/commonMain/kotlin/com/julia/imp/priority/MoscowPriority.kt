package com.julia.imp.priority

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("moscow")
data class MoscowPriority(
    val level: MoscowPriorityLevel = MoscowPriorityLevel.WontHave
) : Priority()