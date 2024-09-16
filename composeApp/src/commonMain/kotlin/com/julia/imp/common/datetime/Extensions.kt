package com.julia.imp.common.datetime

import androidx.compose.material3.DatePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

@OptIn(ExperimentalMaterial3Api::class)
val DatePickerState.selectedDate: LocalDate?
    get() = this.selectedDateMillis
        ?.let { Instant.fromEpochMilliseconds(it) }
        ?.toLocalDateTime(TimeZone.UTC)
        ?.date

/**
 * Sum all durations returned from the [selector] lambda.
 */
inline fun <T> Iterable<T>.sumOfDuration(selector: (T) -> Duration): Duration =
    this.sumOf { selector(it).inWholeMilliseconds }.milliseconds