package com.axemorgan.genconcatalogue.dagger;

import com.axemorgan.genconcatalogue.EventUpdateService;

import dagger.Component;

@Network
@Component(dependencies = AppComponent.class, modules = {NetworkModule.class})
public interface NetworkComponent {
    void inject(EventUpdateService service);
}
