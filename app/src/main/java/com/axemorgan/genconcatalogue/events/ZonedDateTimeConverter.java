package com.axemorgan.genconcatalogue.events;

import android.arch.persistence.room.TypeConverter;

import org.threeten.bp.Instant;
import org.threeten.bp.ZoneOffset;
import org.threeten.bp.ZonedDateTime;

public class ZonedDateTimeConverter {

    @TypeConverter
    public static ZonedDateTime fromEpochSecond(long date) {
        return ZonedDateTime.ofInstant(Instant.ofEpochSecond(date), ZoneOffset.UTC);
    }

    @TypeConverter
    public static long fromZonedDateTime(ZonedDateTime zdt) {
        return zdt.toEpochSecond();
    }
}
