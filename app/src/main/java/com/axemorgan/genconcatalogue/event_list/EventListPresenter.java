package com.axemorgan.genconcatalogue.event_list;

import android.util.Log;

import com.axemorgan.genconcatalogue.events.Event;
import com.axemorgan.genconcatalogue.events.EventDao;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.DisposableSubscriber;

public class EventListPresenter extends EventListContract.Presenter {

    private final EventDao eventDao;

    @Inject
    public EventListPresenter(EventDao eventDao) {
        this.eventDao = eventDao;
    }


    @Override
    public void onViewAttached() {
        eventDao.getAll()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableSubscriber<List<Event>>() {

                    @Override
                    public void onNext(List<Event> events) {
                        getViewOrThrow().showEvents(events);
                    }

                    @Override
                    public void onError(Throwable t) {
                        Log.e("Oops", "Error while fetching items", t);
                    }

                    @Override
                    public void onComplete() {
                        Log.i("Done", "Subscription completed");
                    }
                });
    }

    @Override
    public void onViewDetached() {

    }
}
