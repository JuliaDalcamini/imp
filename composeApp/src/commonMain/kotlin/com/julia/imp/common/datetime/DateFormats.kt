package com.julia.imp.common.datetime

import kotlinx.datetime.LocalDate
import kotlinx.datetime.format.char

object DateFormats {

    /**
     * Default brazilian date format.
     */
    val DEFAULT = LocalDate.Format {
        dayOfMonth()
        char('/')
        monthNumber()
        char('/')
        year()
    }
}