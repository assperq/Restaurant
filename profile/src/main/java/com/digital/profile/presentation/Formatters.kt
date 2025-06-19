package com.digital.profile.presentation

import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format
import kotlinx.datetime.format.char
import kotlinx.datetime.toLocalDateTime

fun getDateFromInstant(instant: LocalDateTime): String {
    val dateFormat = LocalDate.Format {
        dayOfMonth(); char('.'); monthNumber(); char('.'); year()
    }

    return instant
        .date
        .format(dateFormat)
}

fun getTimeFromInstant(instant: LocalDateTime): String {
    val timeFormat = LocalTime.Format {
        hour(); char(':'); minute()
    }
    return instant
        .time
        .format(timeFormat)
}

fun getDateFromInstant(instant: Instant): String {
    val dateFormat = LocalDate.Format {
        dayOfMonth(); char('.'); monthNumber(); char('.'); year()
    }

    return instant
        .toLocalDateTime(TimeZone.currentSystemDefault())
        .date
        .format(dateFormat)
}

fun getTimeFromInstant(instant: Instant): String {
    val timeFormat = LocalTime.Format {
        hour(); char(':'); minute()
    }
    return instant
        .toLocalDateTime(TimeZone.currentSystemDefault())
        .time
        .format(timeFormat)
}