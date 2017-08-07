package com.axemorgan.genconcatalogue.events;

import android.arch.persistence.room.TypeConverter;

import org.threeten.bp.ZonedDateTime;

public class ZonedDateTimeConverter {

    @TypeConverter
    public static ZonedDateTime fromDateString(String date) {
        if (!date.isEmpty()) {
            return ZonedDateTime.parse(date);
        } else {
            return null;
        }
    }

    @TypeConverter
    public static String fromZonedDateTime(ZonedDateTime zdt) {
        if (zdt != null) {
            return zdt.toString();
        } else {
            return "";
        }
    }
}
