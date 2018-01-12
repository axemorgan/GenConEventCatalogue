package com.axemorgan.genconcatalogue;

import android.app.Application;
import android.content.Context;

import com.axemorgan.genconcatalogue.dagger.AppComponent;
import com.axemorgan.genconcatalogue.dagger.AppModule;
import com.axemorgan.genconcatalogue.dagger.DaggerAppComponent;
import com.facebook.stetho.Stetho;

import timber.log.Timber;

public class CatalogueApplication extends Application {

    public static CatalogueApplication get(Context context) {
        return (CatalogueApplication) context.getApplicationContext();
    }


    private AppComponent appComponent;


    @Override
    public void onCreate() {
        super.onCreate();
        System.setProperty("org.apache.poi.javax.xml.stream.XMLInputFactory", "com.fasterxml.aalto.stax.InputFactoryImpl");
        System.setProperty("org.apache.poi.javax.xml.stream.XMLOutputFactory", "com.fasterxml.aalto.stax.OutputFactoryImpl");
        System.setProperty("org.apache.poi.javax.xml.stream.XMLEventFactory", "com.fasterxml.aalto.stax.EventFactoryImpl");

        Timber.plant(new Timber.DebugTree());

        appComponent = DaggerAppComponent
                .builder()
                .appModule(new AppModule(this))
                .build();

        Stetho.initializeWithDefaults(this);
    }

    public AppComponent getAppComponent() {
        return appComponent;
    }
}
