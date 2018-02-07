package com.axemorgan.genconcatalogue.events;

import javax.inject.Inject;

import io.reactivex.Completable;

public class UpdateEvent {

    private final EventDao eventDao;

    @Inject
    public UpdateEvent(EventDao eventDao) {
        this.eventDao = eventDao;
    }

    public Completable updateEvent(Event event) {
        return Completable.fromAction(() -> eventDao.update(event));
    }
}
