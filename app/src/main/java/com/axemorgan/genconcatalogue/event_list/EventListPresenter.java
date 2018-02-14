package com.axemorgan.genconcatalogue.event_list;

import com.axemorgan.genconcatalogue.SearchModel;
import com.axemorgan.genconcatalogue.components.RxUtils;
import com.axemorgan.genconcatalogue.events.Event;
import com.axemorgan.genconcatalogue.events.EventDao;
import com.axemorgan.genconcatalogue.events.Search;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.DisposableSubscriber;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import timber.log.Timber;

public class EventListPresenter extends EventListContract.Presenter implements Function1<SearchModel, Unit> {

    private final SearchModel searchModel;
    private final EventDao eventDao;
    private final Search search;

    private DisposableSubscriber<List<Event>> searchSubscription;
    private DisposableSubscriber<List<Event>> eventFetchSubscription;

    @Inject

    public EventListPresenter(SearchModel searchModel, EventDao eventDao, Search search) {
        this.searchModel = searchModel;
        this.eventDao = eventDao;
        this.search = search;
    }


    @Override
    public void onViewAttached() {
        searchModel.addObserver(this);
        this.fetchAllEvents();
    }

    @Override
    public void onViewDetached() {
        searchModel.removeObserver(this);
        RxUtils.disposeOf(searchSubscription, eventFetchSubscription);
    }

    @Override
    public Unit invoke(final SearchModel searchModel) {
        final String query = searchModel.getQuery();
        searchSubscription = new DisposableSubscriber<List<Event>>() {
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
        };
        search.using(searchModel)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(subscription -> {
                    if (getView() != null) {
                        getViewOrThrow().showLoading();
                    }
                })
                .subscribe(searchSubscription);

        return null;
    }

    @Override
    void onViewDetails(Event event) {
        this.getViewOrThrow().navigateToEventDetail(event);
    }

    private void fetchAllEvents() {
        eventFetchSubscription = new DisposableSubscriber<List<Event>>() {

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
        };
        eventDao.getAll()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(subscription -> {
                    if (getView() != null) {
                        getViewOrThrow().showLoading();
                    }
                })
                .subscribe(eventFetchSubscription);
    }
}
