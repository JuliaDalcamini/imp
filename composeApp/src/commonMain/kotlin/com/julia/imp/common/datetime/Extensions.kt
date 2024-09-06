package com.julia.imp.common.datetime

import androidx.compose.material3.DatePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

@OptIn(ExperimentalMaterial3Api::class)
val DatePickerState.selectedDate: LocalDate?
    get() = this.selectedDateMillis
        ?.let { Instant.fromEpochMilliseconds(it) }
        ?.toLocalDateTime(TimeZone.UTC)
        ?.date