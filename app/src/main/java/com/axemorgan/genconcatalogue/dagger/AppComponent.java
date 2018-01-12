package com.axemorgan.genconcatalogue.dagger;

import android.content.Context;

import com.axemorgan.genconcatalogue.events.EventDao;
import com.axemorgan.genconcatalogue.events.EventUpdateService;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {AppModule.class, NetworkModule.class})
public interface AppComponent {
    Context getContext();

    EventDao getEventDao();

    void inject(EventUpdateService eventUpdateService);
}
