package com.axemorgan.genconcatalogue.dagger;

import android.content.Context;

import com.readystatesoftware.chuck.ChuckInterceptor;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

@Module
public class NetworkModule {

    private static final String BASE_URL = "http://www.gencon.com";

    @Network
    @Provides
    Retrofit provideRetrofit(OkHttpClient client) {
        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .build();
    }

    @Network
    @Provides
    OkHttpClient provideOkHttpClient(Context appContext) {
        return new OkHttpClient.Builder()
                .addInterceptor(new ChuckInterceptor(appContext))
                .build();
    }
}
