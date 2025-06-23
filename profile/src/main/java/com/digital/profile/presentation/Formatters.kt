package com.digital.profile.presentation

import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format
import kotlinx.datetime.format.char
import kotlinx.datetime.toLocalDateTime

val dateFormat = LocalDate.Format {
    dayOfMonth(); char('.'); monthNumber(); char('.'); year()
}

val timeFormat = LocalTime.Format {
    hour(); char(':'); minute()
}

fun getDateFromInstant(instant: LocalDateTime) = instant.date.format(dateFormat)

fun getTimeFromInstant(instant: LocalDateTime) = instant.time.format(timeFormat)

fun getDateFromInstant(instant: Instant) = instant.toLocalDateTime(TimeZone.currentSystemDefault()).date.format(dateFormat)

fun getTimeFromInstant(instant: Instant) = instant.toLocalDateTime(TimeZone.currentSystemDefault()).time.format(timeFormat)

fun formatDate(date : LocalDate) = date.format(dateFormat)