package com.axemorgan.genconcatalogue;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import okhttp3.ResponseBody;
import retrofit2.Response;


public class EventUpdateService extends IntentService {

    private static final String ACTION_UPDATE_EVENTS = "action_update_events";

    public EventUpdateService() {
        super("Event Update Service");
    }

    public static Intent getIntent(Context context) {
        return new Intent(context, EventUpdateService.class).
                setAction(ACTION_UPDATE_EVENTS);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        try {
            EventListClient eventListClient = ((CatalogueApplication)
                    this.getApplication()).getRetrofit().create(EventListClient.class);
            Response<ResponseBody> response = eventListClient.getEventList().execute();
            if (response.isSuccessful()) {
                if (this.writeResponseBodyToDisk(response.body())) {
                    Log.i("EventUpdateService", "Successfully wrote to file");
                } else {
                    Log.e("EventUpdateService", "Failed to write to file");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Parse into model objects

        // Persist into database
    }

    private boolean writeResponseBodyToDisk(ResponseBody body) {
        try {
            File eventsFile = new File(this.getApplicationContext().getFilesDir(), "events.xlsx");

            InputStream inputStream = null;
            OutputStream outputStream = null;

            try {
                byte[] fileReader = new byte[4096];

                long fileSize = body.contentLength();
                long fileSizeDownloaded = 0;

                inputStream = body.byteStream();
                outputStream = new FileOutputStream(eventsFile);

                while (true) {
                    int read = inputStream.read(fileReader);

                    if (read == -1) {
                        break;
                    }

                    outputStream.write(fileReader, 0, read);
                }

                outputStream.flush();

                return true;
            } catch (IOException e) {
                return false;
            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }

                if (outputStream != null) {
                    outputStream.close();
                }
            }
        } catch (IOException e) {
            return false;
        }
    }
}
