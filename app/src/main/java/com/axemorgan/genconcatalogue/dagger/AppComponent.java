package com.axemorgan.genconcatalogue.dagger;

import android.content.Context;

import com.axemorgan.genconcatalogue.events.EventDao;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = AppModule.class)
public interface AppComponent {
    Context getContext();

    EventDao getEventDao();
}
