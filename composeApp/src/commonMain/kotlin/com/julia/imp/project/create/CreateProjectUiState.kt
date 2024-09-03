package com.julia.imp.project.create

import com.julia.imp.priority.MoscowPrioritizer
import com.julia.imp.priority.Prioritizer

data class CreateProjectUiState(
    val name: String = "",
    val prioritizer: Prioritizer = MoscowPrioritizer,
    val totalInspectors: Int = 2,
    val loading: Boolean = false,
    val created: Boolean = false,
    val error: Boolean = false
)