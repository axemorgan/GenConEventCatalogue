package com.axemorgan.genconcatalogue.events;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;

import com.axemorgan.genconcatalogue.CatalogueApplication;
import com.axemorgan.genconcatalogue.events.service.EventsDownloader;
import com.axemorgan.genconcatalogue.events.service.EventsParser;

import org.threeten.bp.format.DateTimeFormatter;

import java.io.File;

import javax.inject.Inject;

import timber.log.Timber;


public class EventUpdateService extends IntentService {

    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("MM/d/y h:m a");
    private static final String ACTION_UPDATE_EVENTS = "action_update_events";

    static final String BROADCAST_UPDATE_EVENTS_STARTED = "BROADCAST_EVENT_UPDATES_STARTED";
    static final String BROADCAST_UPDATE_EVENTS_FINISHED = "BROADCAST_EVENT_UPDATES_FINISHED";

    public static Intent getIntent(Context context) {
        return new Intent(context, EventUpdateService.class).setAction(ACTION_UPDATE_EVENTS);
    }

    @Inject
    EventDao eventDao;
    @Inject
    EventsDownloader downloader;
    @Inject
    EventsParser parser;

    public EventUpdateService() {
        super("Event Update Service");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        CatalogueApplication.get(this).getAppComponent().inject(this);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Timber.i("Event Update Service started");
        LocalBroadcastManager.getInstance(this).sendBroadcast(new Intent(BROADCAST_UPDATE_EVENTS_STARTED));

        downloader.downloadEvents(this.getNewEventsFile());

        parser.parseEvents(this.getEventsFile());

        LocalBroadcastManager.getInstance(this).sendBroadcast(new Intent(BROADCAST_UPDATE_EVENTS_FINISHED));
    }

    private File getNewEventsFile() {
        File eventsFile = new File(this.getApplicationContext().getFilesDir(), "events.xlsx");
        if (eventsFile.exists()) {
            eventsFile.delete();
        }
        return eventsFile;
    }

    private File getEventsFile() {
        return new File(this.getApplicationContext().getFilesDir(), "events.xlsx");
    }
}
