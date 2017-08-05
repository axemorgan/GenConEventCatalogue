package com.axemorgan.genconcatalogue.events;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;

public class EventUpdateBroadcastReceiver extends BroadcastReceiver {

    public interface Listener {
        void onUpdateStarted();

        void onUpdateFinished();
    }


    private final Listener listener;


    public EventUpdateBroadcastReceiver(Listener listener) {
        this.listener = listener;
    }

    public void register(Context context) {
        LocalBroadcastManager.getInstance(context).registerReceiver(this, getIntentFilter());
    }

    public void unregister(Context context) {
        LocalBroadcastManager.getInstance(context).unregisterReceiver(this);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        switch (intent.getAction()) {
            case EventUpdateService.BROADCAST_UPDATE_EVENTS_STARTED: {
                listener.onUpdateStarted();
                break;
            }
            case EventUpdateService.BROADCAST_UPDATE_EVENTS_FINISHED: {
                listener.onUpdateFinished();
                break;
            }
            default: {
                throw new IllegalArgumentException("Received unknown action");
            }
        }
    }

    private IntentFilter getIntentFilter() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(EventUpdateService.BROADCAST_UPDATE_EVENTS_STARTED);
        intentFilter.addAction(EventUpdateService.BROADCAST_UPDATE_EVENTS_FINISHED);
        return intentFilter;
    }
}