package com.axemorgan.genconcatalogue.dagger;

import com.axemorgan.genconcatalogue.event_detail.EventDetailActivity;
import com.axemorgan.genconcatalogue.event_list.EventListFragment;

import dagger.Component;

@PerFragment
@Component(dependencies = AppComponent.class, modules = EventListModule.class)
public interface EventListComponent {
    void inject(EventListFragment fragment);

    void inject(EventDetailActivity activity);
}
