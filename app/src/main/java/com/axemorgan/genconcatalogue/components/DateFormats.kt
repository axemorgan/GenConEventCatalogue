package com.axemorgan.genconcatalogue.components

import org.threeten.bp.ZonedDateTime
import org.threeten.bp.format.DateTimeFormatter
import org.threeten.bp.format.FormatStyle
import org.threeten.bp.temporal.TemporalAccessor

object DateFormats {

    private const val dateRangeSeparator = "\u2014"
    private val startDateFormatter = lazy { DateTimeFormatter.ofPattern("EEEE, LLL d h:mma") }
    private val endDateFormatter = lazy { DateTimeFormatter.ofPattern("h:mma") }
    private val longEndDateFormatter = lazy { DateTimeFormatter.ofPattern("EEEE, LLL d h:mma") }
    private val localTimeHourFormatter = lazy { DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT) }

    private val dayFormat = lazy { DateTimeFormatter.ofPattern("EEEE") }

    fun formatLongDateRange(startDate: ZonedDateTime, endDate: ZonedDateTime): String {
        return if (startDate.dayOfMonth == endDate.dayOfMonth) {
            startDate.format(startDateFormatter.value) +
                    dateRangeSeparator +
                    endDate.format(endDateFormatter.value)
        } else {
            startDate.format(startDateFormatter.value) +
                    dateRangeSeparator +
                    endDate.format(longEndDateFormatter.value)
        }
    }

    fun formatDay(date: TemporalAccessor): String {
        return dayFormat.value.format(date)
    }

    fun formatToHour(time: TemporalAccessor): String {
        return localTimeHourFormatter.value.format(time)
    }
}