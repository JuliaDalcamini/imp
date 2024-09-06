package com.julia.imp.project.create

import com.julia.imp.priority.MoscowPrioritizer
import com.julia.imp.priority.Prioritizer
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.todayIn

data class CreateProjectUiState(
    val name: String = "",
    val targetDate: LocalDate = Clock.System.todayIn(TimeZone.currentSystemDefault()),
    val prioritizer: Prioritizer = MoscowPrioritizer,
    val minInspectors: Int = 2,
    val loading: Boolean = false,
    val created: Boolean = false,
    val error: Boolean = false
)