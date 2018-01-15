package com.axemorgan.genconcatalogue.dagger;

import android.arch.persistence.room.Room;
import android.content.Context;

import com.axemorgan.genconcatalogue.SearchActivityContract;
import com.axemorgan.genconcatalogue.SearchModel;
import com.axemorgan.genconcatalogue.SearchPresenter;
import com.axemorgan.genconcatalogue.event_detail.EventDetailContract;
import com.axemorgan.genconcatalogue.event_detail.EventDetailPresenter;
import com.axemorgan.genconcatalogue.event_list.EventListContract;
import com.axemorgan.genconcatalogue.event_list.EventListPresenter;
import com.axemorgan.genconcatalogue.events.EventDao;
import com.axemorgan.genconcatalogue.events.EventDatabase;
import com.axemorgan.genconcatalogue.filters.FilterContract;
import com.axemorgan.genconcatalogue.filters.FilterPresenter;

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

    @Provides
    SearchActivityContract.Presenter provideSearchPresenter(SearchModel model) {
        return new SearchPresenter(model);
    }

    @Provides
    EventListContract.Presenter provideEventListPresenter(SearchModel model, EventDao eventDao) {
        return new EventListPresenter(model, eventDao);
    }

    @Provides
    EventDetailContract.Presenter provideEventDetailPresenter(EventDao eventDao) {
        return new EventDetailPresenter(eventDao);
    }

    @Provides
    FilterContract.Presenter provideFilterPresenter(EventDao eventDao, SearchModel searchModel) {
        return new FilterPresenter(eventDao, searchModel);
    }
}
