package com.axemorgan.genconcatalogue.event_list;

import com.axemorgan.genconcatalogue.components.AbstractPresenter;
import com.axemorgan.genconcatalogue.events.Event;

import java.util.List;

public interface EventListContract {
    interface View {
        void showEvents(List<Event> events);
    }

    abstract class Presenter extends AbstractPresenter<View> {

    }
}
