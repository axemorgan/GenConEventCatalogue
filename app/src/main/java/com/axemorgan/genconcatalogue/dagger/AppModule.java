package com.axemorgan.genconcatalogue.dagger;

import android.arch.persistence.room.Room;
import android.content.Context;

import com.axemorgan.genconcatalogue.EventDatabase;
import com.axemorgan.genconcatalogue.events.EventDao;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {

    private Context appContext;


    public AppModule(Context appContext) {
        this.appContext = appContext;
    }

    @Singleton
    @Provides
    Context provideAppContext() {
        return appContext;
    }

    @Singleton
    @Provides
    EventDatabase provideAppDatabase(Context context) {
        return Room.databaseBuilder(context, EventDatabase.class, "events").build();
    }

    @Singleton
    @Provides
    EventDao provideEventDao(EventDatabase database) {
        return database.eventDao();
    }
}
