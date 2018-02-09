package com.axemorgan.genconcatalogue.dagger;

import android.content.Context;

import com.axemorgan.genconcatalogue.MainActivity;
import com.axemorgan.genconcatalogue.event_detail.EventDetailActivity;
import com.axemorgan.genconcatalogue.event_list.EventListFragment;
import com.axemorgan.genconcatalogue.events.EventDao;
import com.axemorgan.genconcatalogue.events.EventUpdateService;
import com.axemorgan.genconcatalogue.filters.FilterFragment;
import com.axemorgan.genconcatalogue.schedule.DayFragment;
import com.axemorgan.genconcatalogue.schedule.ScheduleFragment;

import org.jetbrains.annotations.NotNull;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {AppModule.class, NetworkModule.class})
public interface AppComponent {
    Context getContext();

    EventDao getEventDao();

    void inject(EventUpdateService eventUpdateService);

    void inject(MainActivity mainActivity);

    void inject(EventListFragment fragment);

    void inject(EventDetailActivity activity);

    void inject(FilterFragment filterFragment);

    void inject(ScheduleFragment scheduleFragment);

    void inject(@NotNull DayFragment dayFragment);
}
