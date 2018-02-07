package com.axemorgan.genconcatalogue.event_detail;

import com.axemorgan.genconcatalogue.components.DateFormats;
import com.axemorgan.genconcatalogue.events.Event;
import com.axemorgan.genconcatalogue.events.EventDao;
import com.axemorgan.genconcatalogue.events.UpdateEvent;

import javax.inject.Inject;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class EventDetailPresenter extends EventDetailContract.Presenter {

    private final EventDao eventDao;
    private final UpdateEvent updateEvent;
    private Event event;

    @Inject
    public EventDetailPresenter(EventDao eventDao, UpdateEvent updateEvent) {
        this.eventDao = eventDao;
        this.updateEvent = updateEvent;
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
                            EventDetailContract.View view = getViewOrThrow();
                            view.showEventTitle(event.getTitle());
                            view.showLongDescription(event.getLongDescription());
                            view.showShortDescription(event.getShortDescription());
                            view.showDate(DateFormats.INSTANCE.formatLongDateRange(
                                    event.getStartDate(), event.getEndDate()));
                            view.showEventType(event.getEventType());
                            view.showPlayerCount(event.getMinimumPlayers(), event.getMaximumPlayers());
                            view.showLocation(getEventLocationString(event));
                            view.showAgeRequirement(event.getAgeRequired());
                            view.showExperienceRequirement(event.getExperienceRequired());
                            view.showMaterialsProvided(event.getMaterialsProvided());
                            view.showAvailableTickets("Tickets $" + event.getCost() + " each. "
                                    + event.getAvailableTickets() + " of " + event.getMaximumPlayers() + " Tickets Available");

                            if (event.getGameSystem().isEmpty()) {
                                view.hideSystem();
                            } else if (event.getRulesEdition().isEmpty()) {
                                view.showSystemAndEdition(event.getGameSystem());
                            } else {
                                view.showSystemAndEdition(event.getGameSystem() + " " + event.getRulesEdition() + " Edition");
                            }

                            if (event.getGroup().isEmpty()) {
                                view.hideGroupName();
                            } else {
                                view.showGroupName(event.getGroup());
                            }

                            if (event.getWebsite().isEmpty()) {
                                view.hideGroupWebsite();
                            } else {
                                view.showGroupWebsite(event.getWebsite());
                            }

                            if (event.getEmail().isEmpty()) {
                                view.hideContactEmail();
                            } else {
                                view.showContactEmail(event.getEmail());
                            }

                            if (event.isSaved()) {
                                view.showForgetEventButton();
                            } else {
                                view.showSaveEventButton();
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

    @Override
    void onSaveEvent() {
        event.setSaved(!event.isSaved());
        updateEvent.updateEvent(event).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {
                    if (getView() != null) {
                        if (event.isSaved()) {
                            getViewOrThrow().showForgetEventButton();
                        } else {
                            getViewOrThrow().showSaveEventButton();
                        }
                    }
                });
    }

    private String getEventLocationString(Event event) {
        String location = event.getLocation();

        if (!event.getRoomName().isEmpty()) {
            location += ": " + event.getRoomName();
        }

        if (!event.getTableNumber().isEmpty()) {
            location += ", Table " + event.getTableNumber();
        }

        return location;
    }
}
