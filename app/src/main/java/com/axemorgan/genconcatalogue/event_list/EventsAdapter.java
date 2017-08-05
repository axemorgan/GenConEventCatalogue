package com.axemorgan.genconcatalogue.event_list;

import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.axemorgan.genconcatalogue.R;
import com.axemorgan.genconcatalogue.events.Event;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

class EventsAdapter extends RecyclerView.Adapter<EventsAdapter.EventHolder> {

    private List<Event> events = Collections.emptyList();

    @Override
    public EventHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new EventHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.event_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(EventHolder holder, int position) {
        Event event = events.get(position);

        holder.name.setText(event.getId());
    }


    @Override
    public int getItemCount() {
        return events.size();
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

        EventHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
