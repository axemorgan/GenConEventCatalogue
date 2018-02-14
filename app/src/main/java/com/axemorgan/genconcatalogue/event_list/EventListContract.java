package com.axemorgan.genconcatalogue.event_list;

import com.axemorgan.genconcatalogue.components.AbstractPresenter;
import com.axemorgan.genconcatalogue.events.Event;

import java.util.List;

public interface EventListContract {
    interface View {
        void showLoading();

        void showEvents(List<Event> events);

        void showNoEventsFound();

        void navigateToEventDetail(Event event);
    }

    abstract class Presenter extends AbstractPresenter<View> {
        abstract void onViewDetails(Event event);
    }
}
