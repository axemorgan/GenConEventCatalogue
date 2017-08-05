package com.axemorgan.genconcatalogue.events;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;

public interface EventListClient {

    @GET("/downloads/events.xlsx")
    public Call<ResponseBody> getEventList();
}
