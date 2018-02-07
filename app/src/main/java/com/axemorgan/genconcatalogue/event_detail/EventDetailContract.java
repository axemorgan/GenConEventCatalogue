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

        void showEventType(String eventType);

        void showSystemAndEdition(String system);

        void hideSystem();

        void showPlayerCount(int min, int max);

        void showLocation(String location);

        void showGroupWebsite(String website);

        void hideGroupWebsite();

        void showContactEmail(String email);

        void hideContactEmail();

        void showAgeRequirement(String requirement);

        void showExperienceRequirement(String requirement);

        void showMaterialsProvided(boolean provided);

        void showAvailableTickets(String ticketsText);

        void showSaveEventButton();

        void showForgetEventButton();

        void launchCalendarIntent(long startTime, long endTime, String title, String location, String description);


    }

    abstract class Presenter extends AbstractPresenter<View> {
        abstract void onAddToCalendar();

        abstract void onSaveEvent();
    }
}
