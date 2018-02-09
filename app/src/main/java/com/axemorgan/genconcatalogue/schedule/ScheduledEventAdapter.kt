package com.axemorgan.genconcatalogue.schedule

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.axemorgan.genconcatalogue.R
import com.axemorgan.genconcatalogue.events.Event

class ScheduledEventAdapter() : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var events = emptyList<Event>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    init {
        setHasStableIds(true)
    }

    override fun getItemId(position: Int): Long {
        return events[position].id.hashCode().toLong()
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder {
        return ScheduledEventViewHolder(LayoutInflater.from(parent?.context)
                .inflate(R.layout.schedule_event_item, parent, false))
    }

    override fun getItemCount(): Int {
        return events.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        val event = events[position]
        if (holder is ScheduledEventViewHolder) {
            holder.title.text = event.title
            holder.location.text = event.location
            holder.duration.text = event.duration.toString()
        }
    }

}