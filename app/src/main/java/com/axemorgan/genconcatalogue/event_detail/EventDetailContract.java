package com.axemorgan.genconcatalogue.event_detail;

import com.axemorgan.genconcatalogue.components.AbstractPresenter;

public interface EventDetailContract {

    interface View {

        String getEventId();

        void showEventTitle(String title);

        void showGroupName(String groupName);

        void hideGroupName();

        void showDate(String date);

        void showShortDescription(String shortDescription);

        void showLongDescription(String longDescription);

        void launchCalendarIntent(long startTime, long endTime, String title, String location, String description);
    }

    abstract class Presenter extends AbstractPresenter<View> {
        abstract void onAddToCalendar();
    }
}
