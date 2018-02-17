package com.axemorgan.genconcatalogue.event_list;

import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.axemorgan.genconcatalogue.R;
import com.axemorgan.genconcatalogue.components.DateFormats;
import com.axemorgan.genconcatalogue.events.Event;

import org.threeten.bp.ZoneId;
import org.threeten.bp.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

class EventsAdapter extends RecyclerView.Adapter<EventsAdapter.EventHolder> {

    interface Listener {
        void onEventPressed(Event event);
    }

    private final Listener listener;
    private List<Event> events = Collections.emptyList();


    EventsAdapter(Listener listener) {
        this.listener = listener;
        this.setHasStableIds(true);
    }

    @Override
    public EventHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final EventHolder eventHolder = new EventHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.event_list_item, parent, false));
        eventHolder.itemView.setOnClickListener(v -> {
            if (eventHolder.getAdapterPosition() != RecyclerView.NO_POSITION) {
                listener.onEventPressed(events.get(eventHolder.getAdapterPosition()));
            }
        });
        return eventHolder;
    }

    @Override
    public void onBindViewHolder(EventHolder holder, int position) {
        Event event = events.get(position);

        holder.name.setText(event.getTitle());

        holder.description.setText(event.getShortDescription());

        if (event.getStartDate() != null && event.getEndDate() != null) {

            holder.eventTime.setText(holder.itemView.getContext().getString(R.string.event_list_item_date,
                    event.getStartDate().format(DateTimeFormatter.ofPattern("E").withZone(ZoneId.systemDefault())),
                    DateFormats.INSTANCE.formatToHour(event.getStartDate()),
                    DateFormats.INSTANCE.formatToHour(event.getEndDate())));
        }

        holder.ticketCount.setText(holder.itemView.getContext().getString(R.string.event_list_item_available_tickets, event.getAvailableTickets(), event.getMaximumPlayers()));
    }


    @Override
    public int getItemCount() {
        return events.size();
    }

    @Override
    public long getItemId(int position) {
        return events.get(position).getId().hashCode();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));
    }

    public void setEvents(List<Event> events) {
        this.events = new ArrayList<>(events);
        this.notifyDataSetChanged();
    }


    static class EventHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.event_name)
        TextView name;
        @BindView(R.id.event_description)
        TextView description;
        @BindView(R.id.event_time)
        TextView eventTime;
        @BindView(R.id.event_ticket_count)
        TextView ticketCount;

        EventHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
