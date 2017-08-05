package com.axemorgan.genconcatalogue;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.axemorgan.genconcatalogue.events.Event;
import com.axemorgan.genconcatalogue.events.EventDao;

@Database(entities = {Event.class}, version = 1)
public abstract class EventDatabase extends RoomDatabase {
    public abstract EventDao eventDao();
}
