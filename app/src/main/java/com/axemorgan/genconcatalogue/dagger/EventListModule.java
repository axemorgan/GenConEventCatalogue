package com.axemorgan.genconcatalogue.dagger;

import com.axemorgan.genconcatalogue.event_list.EventListContract;
import com.axemorgan.genconcatalogue.event_list.EventListPresenter;
import com.axemorgan.genconcatalogue.events.EventDao;

import dagger.Module;
import dagger.Provides;

@Module
public class EventListModule {

    @PerFragment
    @Provides
    public EventListContract.Presenter providePresenter(EventDao eventDao) {
        return new EventListPresenter(eventDao);
    }
}
