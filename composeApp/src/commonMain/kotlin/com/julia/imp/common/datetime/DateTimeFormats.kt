package com.julia.imp.common.datetime

import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.format.char

object DateTimeFormats {

    /**
     * Default brazilian date time format.
     */
    val DEFAULT = LocalDateTime.Format {
        dayOfMonth()
        char('/')
        monthNumber()
        char('/')
        year()
        char(' ')
        hour()
        char(':')
        minute()
        char(':')
        second()
    }
}