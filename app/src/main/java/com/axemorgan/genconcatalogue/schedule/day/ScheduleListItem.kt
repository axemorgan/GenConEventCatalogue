package com.axemorgan.genconcatalogue.schedule.day

import org.threeten.bp.LocalTime


sealed class ScheduleListItem {

    abstract fun getSortValue(): Int

    data class Event(val event: com.axemorgan.genconcatalogue.events.Event) : ScheduleListItem() {
        override fun getSortValue(): Int {
            return event.startDate.hour
        }
    }

    data class HourHeader(val time: LocalTime) : ScheduleListItem() {
        override fun getSortValue(): Int {
            return time.hour
        }
    }

    object Comparator : kotlin.Comparator<ScheduleListItem> {
        override fun compare(left: ScheduleListItem?, right: ScheduleListItem?): Int {
            if (left == right) return 0

            if (left == null) {
                return 1
            } else if (right == null) {
                return -1
            }

            when (left) {
                is Event -> {
                    val comparison = left.getSortValue() - right.getSortValue()
                    return if (comparison == 0 && right is HourHeader) {
                        1
                    } else {
                        comparison
                    }
                }
                is HourHeader -> {
                    val comparison = left.getSortValue() - right.getSortValue()
                    return if (comparison == 0 && right is Event) {
                        -1
                    } else {
                        comparison
                    }
                }
            }
        }
    }
}
