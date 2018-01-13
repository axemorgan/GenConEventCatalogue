package com.axemorgan.genconcatalogue.dagger;

import android.content.Context;

import com.axemorgan.genconcatalogue.events.EventListClient;
import com.readystatesoftware.chuck.ChuckInterceptor;

import dagger.Module;
import dagger.Provides;
import dagger.Reusable;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

@Module
class NetworkModule {

    private static final String BASE_URL = "http://www.gencon.com";

    @Reusable
    @Provides
    Retrofit provideRetrofit(OkHttpClient client) {
        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .build();
    }

    @Reusable
    @Provides
    EventListClient provideEventListClient(Retrofit retrofit) {
        return retrofit.create(EventListClient.class);
    }

    @Reusable
    @Provides
    OkHttpClient provideOkHttpClient(Context appContext) {
        return new OkHttpClient.Builder()
                .addInterceptor(new ChuckInterceptor(appContext))
                .build();
    }
}
