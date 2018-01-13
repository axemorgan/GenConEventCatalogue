package com.axemorgan.genconcatalogue.components

import org.threeten.bp.ZonedDateTime
import org.threeten.bp.format.DateTimeFormatter

object DateFormats {

    private val dateRangeSeparator = "\u2014"
    private val startDateFormatter = lazy { DateTimeFormatter.ofPattern("EEEE, LLL d h:mma") }
    private val endDateFormatter = lazy { DateTimeFormatter.ofPattern("h:mma") }
    private val longEndDateFormatter = lazy { DateTimeFormatter.ofPattern("EEEE, LLL d h:mma") }

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
}