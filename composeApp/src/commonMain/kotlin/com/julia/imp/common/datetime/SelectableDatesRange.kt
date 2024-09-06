package com.julia.imp.common.datetime

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SelectableDates
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

@OptIn(ExperimentalMaterial3Api::class)
class SelectableDatesRange(private val minDate: LocalDate) : SelectableDates {
    
    override fun isSelectableDate(utcTimeMillis: Long): Boolean {
        val date = Instant.fromEpochMilliseconds(utcTimeMillis)
            .toLocalDateTime(TimeZone.UTC)
            .date
        
        return date >= minDate
    }
    
    override fun isSelectableYear(year: Int): Boolean =
        year >= minDate.year
}