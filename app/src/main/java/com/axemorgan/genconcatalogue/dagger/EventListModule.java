package com.axemorgan.genconcatalogue.dagger;

import com.axemorgan.genconcatalogue.event_list.EventListContract;
import com.axemorgan.genconcatalogue.event_list.EventListPresenter;

import dagger.Module;
import dagger.Provides;

@Module
public class EventListModule {

    @PerFragment
    @Provides
    public EventListContract.Presenter providePresenter() {
        return new EventListPresenter();
    }
}
