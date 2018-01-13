package com.axemorgan.genconcatalogue.event_detail;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.axemorgan.genconcatalogue.CatalogueApplication;
import com.axemorgan.genconcatalogue.R;
import com.axemorgan.genconcatalogue.events.Event;
import com.axemorgan.genconcatalogue.events.EventDao;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class EventDetailActivity extends AppCompatActivity {

    private static final String EXTRA_EVENT_ID = "extra_event_id";

    public static Intent forEvent(Context context, String eventId) {
        return new Intent(context, EventDetailActivity.class)
                .putExtra(EXTRA_EVENT_ID, eventId);
    }


    @BindView(R.id.event_detail_title)
    TextView title;
    @BindView(R.id.event_detail_short_description)
    TextView shortDescription;
    @BindView(R.id.event_detail_long_description)
    TextView longDescription;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Inject
    EventDao eventDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);
        ButterKnife.bind(this);

        this.setSupportActionBar(toolbar);
        ActionBar actionBar = this.getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        CatalogueApplication.get(this).getAppComponent().inject(this);

        String eventId = this.getIntent().getStringExtra(EXTRA_EVENT_ID);
        Single<Event> eventSingle = eventDao.byId(eventId);
        eventSingle.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableSingleObserver<Event>() {
                    @Override
                    public void onSuccess(@NonNull Event event) {
                        showEvent(event);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Timber.e(e);
                    }
                });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                this.finish();
                return true;
            }
            default: {
                return super.onOptionsItemSelected(item);
            }
        }
    }

    private void showEvent(Event event) {
        title.setText(event.getTitle());
        shortDescription.setText(event.getShortDescription());
        longDescription.setText(event.getLongDescription());
    }
}
