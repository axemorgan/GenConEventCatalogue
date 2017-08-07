package com.axemorgan.genconcatalogue.events;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;

@Database(entities = {Event.class}, version = 1)
@TypeConverters(ZonedDateTimeConverter.class)
public abstract class EventDatabase extends RoomDatabase {
    public abstract EventDao eventDao();
}
