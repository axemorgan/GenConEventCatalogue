package com.axemorgan.genconcatalogue.event_detail;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.axemorgan.genconcatalogue.CatalogueApplication;
import com.axemorgan.genconcatalogue.R;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class EventDetailActivity extends AppCompatActivity implements EventDetailContract.View {

    private static final String EXTRA_EVENT_ID = "extra_event_id";

    public static Intent forEvent(Context context, String eventId) {
        return new Intent(context, EventDetailActivity.class)
                .putExtra(EXTRA_EVENT_ID, eventId);
    }


    @BindView(R.id.event_detail_title)
    TextView titleField;
    @BindView(R.id.event_detail_group_name)
    TextView groupNameField;
    @BindView(R.id.event_detail_event_type)
    TextView eventTypeField;
    @BindView(R.id.event_detail_system)
    TextView systemField;
    @BindView(R.id.event_detail_date_time)
    TextView dateField;
    @BindView(R.id.event_detail_player_count)
    TextView playerCountField;
    @BindView(R.id.event_detail_short_description)
    TextView shortDescriptionField;
    @BindView(R.id.event_detail_long_description)
    TextView longDescriptionField;
    @BindView(R.id.event_detail_location)
    TextView locationField;
    @BindView(R.id.event_detail_group_website)
    TextView groupWebsiteField;
    @BindView(R.id.event_detail_contact_email)
    TextView contactEmailField;
    @BindView(R.id.event_detail_age_requirement)
    TextView ageRequirementField;
    @BindView(R.id.event_detail_experience_requirement)
    TextView experienceRequirementField;
    @BindView(R.id.event_detail_materials_provided)
    TextView materialsProvidedField;
    @BindView(R.id.event_detail_tickets)
    TextView ticketsField;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Inject
    EventDetailContract.Presenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);
        ButterKnife.bind(this);

        this.setSupportActionBar(toolbar);
        ActionBar actionBar = this.getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("");

        CatalogueApplication.get(this).getAppComponent().inject(this);

        presenter.attachView(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.detachView();
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

    @Override
    public String getEventId() {
        return this.getIntent().getStringExtra(EXTRA_EVENT_ID);
    }

    @Override
    public void showEventTitle(String title) {
        titleField.setText(title);
    }

    @Override
    public void showGroupName(String groupName) {
        groupNameField.setVisibility(View.VISIBLE);
        groupNameField.setText(groupName);
    }

    @Override
    public void hideGroupName() {
        groupNameField.setVisibility(View.GONE);
    }

    @Override
    public void showDate(String date) {
        dateField.setText(date);
    }

    @Override
    public void showShortDescription(String shortDescription) {
        shortDescriptionField.setText(shortDescription);

    }

    @Override
    public void showLongDescription(String longDescription) {
        longDescriptionField.setText(longDescription);
    }

    @Override
    public void showEventType(String eventType) {
        eventTypeField.setText(eventType);
    }

    @Override
    public void showSystemAndEdition(String system) {
        systemField.setVisibility(View.VISIBLE);
        systemField.setText(system);
    }

    @Override
    public void hideSystem() {
        systemField.setVisibility(View.GONE);
    }

    @Override
    public void showPlayerCount(int min, int max) {
        playerCountField.setText(getString(R.string.event_detail_player_count, min, max));
    }

    @Override
    public void showLocation(String location) {
        locationField.setText(location);
    }

    @Override
    public void showGroupWebsite(String website) {
        groupWebsiteField.setVisibility(View.VISIBLE);
        groupWebsiteField.setText(website);
    }

    @Override
    public void hideGroupWebsite() {
        groupWebsiteField.setVisibility(View.GONE);
    }

    @Override
    public void showContactEmail(String email) {
        contactEmailField.setVisibility(View.VISIBLE);
        contactEmailField.setText(email);
    }

    @Override
    public void hideContactEmail() {
        contactEmailField.setVisibility(View.GONE);
    }

    @Override
    public void showAgeRequirement(String requirement) {
        ageRequirementField.setText(requirement);
    }

    @Override
    public void showExperienceRequirement(String requirement) {
        experienceRequirementField.setText(requirement);
    }

    @Override
    public void showMaterialsProvided(boolean provided) {
        if (provided) {
            materialsProvidedField.setText(R.string.event_detail_materials_provided);
        } else {
            materialsProvidedField.setText(R.string.event_detail_materials_not_provided);
        }
    }

    @Override
    public void showAvailableTickets(String ticketsText) {

    }

    @Override
    public void launchCalendarIntent(long startTime, long endTime, String title, String location, String description) {
        this.startActivity(new Intent(Intent.ACTION_INSERT)
                .setData(CalendarContract.Events.CONTENT_URI)
                .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, startTime)
                .putExtra(CalendarContract.EXTRA_EVENT_END_TIME, endTime)
                .putExtra(CalendarContract.Events.TITLE, title)
                .putExtra(CalendarContract.Events.EVENT_LOCATION, location)
                .putExtra(CalendarContract.Events.DESCRIPTION, description));
    }

    @OnClick(R.id.event_detail_date_time)
    public void onDateTimeClicked() {
        presenter.onAddToCalendar();
    }
}
