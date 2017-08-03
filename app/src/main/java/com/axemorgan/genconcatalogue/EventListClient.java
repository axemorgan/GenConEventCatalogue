package com.axemorgan.genconcatalogue;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;

public interface EventListClient {

    public static final String BASE_URL = "http://www.gencon.com";

    @GET("/downloads/events.xlsx")
    public Call<ResponseBody> getEventList();
}
