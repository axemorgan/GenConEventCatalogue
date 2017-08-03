package com.axemorgan.genconcatalogue;

import android.app.Application;

import com.readystatesoftware.chuck.ChuckInterceptor;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

public class CatalogueApplication extends Application {

    private Retrofit retrofit;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public Retrofit getRetrofit() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(EventListClient.BASE_URL)
                    .client(new OkHttpClient.Builder()
                            .addInterceptor(new ChuckInterceptor(this))
                            .build())
                    .build();
        }

        return retrofit;
    }
}
