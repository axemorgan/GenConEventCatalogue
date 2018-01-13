package com.axemorgan.genconcatalogue.event_list;

import com.axemorgan.genconcatalogue.SearchModel;
import com.axemorgan.genconcatalogue.events.Event;
import com.axemorgan.genconcatalogue.events.EventDao;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.DisposableSubscriber;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import timber.log.Timber;

public class EventListPresenter extends EventListContract.Presenter implements Function1<String, Unit> {

    private final SearchModel searchModel;
    private final EventDao eventDao;

    @Inject
    public EventListPresenter(SearchModel searchModel, EventDao eventDao) {
        this.searchModel = searchModel;
        this.eventDao = eventDao;
    }


    @Override
    public void onViewAttached() {
        searchModel.addObserver(this);

        eventDao.getAll()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableSubscriber<List<Event>>() {

                    @Override
                    public void onNext(List<Event> events) {
                        if (events.isEmpty()) {
                            getViewOrThrow().showNoEventsFound();
                        } else {
                            getViewOrThrow().showEvents(events);
                        }
                    }

                    @Override
                    public void onError(Throwable t) {
                        Timber.e(t, "Error while fetching items");
                    }

                    @Override
                    public void onComplete() {
                        Timber.i("Subscription completed");
                    }
                });
    }

    @Override
    public void onViewDetached() {
        searchModel.removeObserver(this);
    }

    @Override
    public Unit invoke(final String query) {
        Timber.i("Searching for %s", query);
        eventDao.search("%" + query + "%")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableSubscriber<List<Event>>() {
                    @Override
                    public void onNext(List<Event> events) {
                        Timber.i("Found %d events for query %s", events.size(), query);
                        if (getView() != null) {
                            if (events.isEmpty()) {
                                getViewOrThrow().showNoEventsFound();
                            } else {
                                getViewOrThrow().showEvents(events);
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable t) {
                        Timber.e(t, "Error while searching events");
                    }

                    @Override
                    public void onComplete() {
                        Timber.i("Subscription ended");
                    }
                });

        return null;
    }

    @Override
    void onViewDetails(Event event) {
        this.getViewOrThrow().navigateToEventDetail(event);
    }
}
