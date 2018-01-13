package com.axemorgan.genconcatalogue.event_detail;

import com.axemorgan.genconcatalogue.components.DateFormats;
import com.axemorgan.genconcatalogue.events.Event;
import com.axemorgan.genconcatalogue.events.EventDao;

import javax.inject.Inject;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class EventDetailPresenter extends EventDetailContract.Presenter {

    private final EventDao eventDao;
    private Event event;

    @Inject
    public EventDetailPresenter(EventDao eventDao) {
        this.eventDao = eventDao;
    }

    @Override
    protected void onViewAttached() {
        super.onViewAttached();

        Single<Event> eventSingle = eventDao.byId(this.getViewOrThrow().getEventId());
        eventSingle.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableSingleObserver<Event>() {
                    @Override
                    public void onSuccess(@NonNull Event event) {
                        EventDetailPresenter.this.event = event;
                        if (getView() != null) {
                            getViewOrThrow().showEventTitle(event.getTitle());
                            getViewOrThrow().showLongDescription(event.getLongDescription());
                            getViewOrThrow().showShortDescription(event.getShortDescription());
                            getViewOrThrow().showDate(DateFormats.INSTANCE.formatLongDateRange(
                                    event.getStartDate(), event.getEndDate()));

                            if (event.getGroup().isEmpty()) {
                                getViewOrThrow().hideGroupName();
                            } else {
                                getViewOrThrow().showGroupName(event.getGroup());
                            }
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Timber.e(e);
                    }
                });
    }

    @Override
    void onAddToCalendar() {
        this.getViewOrThrow().launchCalendarIntent(
                event.getStartDate().toEpochSecond() * 1000,
                event.getEndDate().toEpochSecond() * 1000,
                event.getTitle(),
                this.getEventLocationString(event),
                event.getShortDescription());
    }

    private String getEventLocationString(Event event) {
        String location = event.getLocation();

        if (!event.getRoomName().isEmpty()) {
            location += ": " + event.getRoomName();
        }

        if (!event.getTableNumber().isEmpty()) {
            location += ", table " + event.getTableNumber();
        }

        return location;
    }
}
