package com.axemorgan.genconcatalogue.schedule.day

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.axemorgan.genconcatalogue.R
import com.axemorgan.genconcatalogue.components.DateFormats
import com.axemorgan.genconcatalogue.events.Event
import kotlin.math.roundToInt

class ScheduledEventAdapter(private val eventClickListener: (Event) -> Unit) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val items = ArrayList<ScheduleListItem>()

    init {
        setHasStableIds(true)
    }

    override fun getItemId(position: Int): Long {
        val item = items[position]
        return when (item) {
            is ScheduleListItem.Event -> item.event.id.hashCode().toLong()
            is ScheduleListItem.HourHeader -> item.time.hour.toLong()
        }
    }

    override fun getItemViewType(position: Int): Int {
        val listItem = items[position]
        return when (listItem) {
            is ScheduleListItem.Event -> R.layout.schedule_event_item
            is ScheduleListItem.HourHeader -> R.layout.schedule_header_item
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder {
        when (viewType) {
            R.layout.schedule_event_item -> {
                val holder = ScheduledEventViewHolder(LayoutInflater.from(parent?.context)
                        .inflate(R.layout.schedule_event_item, parent, false))
                holder.itemView.setOnClickListener {
                    val item = items[holder.adapterPosition]
                    if (holder.adapterPosition != RecyclerView.NO_POSITION) {
                        eventClickListener.invoke((item as ScheduleListItem.Event).event)
                    }
                }
                return holder
            }
            R.layout.schedule_header_item -> {
                val view = LayoutInflater.from(parent?.context)
                        .inflate(R.layout.schedule_header_item, parent, false)
                return ScheduleHeaderViewHolder(view)
            }
            else -> {
                throw IllegalStateException("Can't understand view type $viewType")
            }
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        when (holder) {
            is ScheduledEventViewHolder -> {
                val event = (items[position] as ScheduleListItem.Event).event
                holder.title.text = event.title
                holder.location.text = event.location
                holder.duration.text = holder.itemView.context.resources.getQuantityString(R.plurals.scheduled_event_duration, (event.duration + 0.5).roundToInt(), event.duration.toString())
            }
            is ScheduleHeaderViewHolder -> {
                val headerTime = (items[position] as ScheduleListItem.HourHeader).time
                holder.time.text = DateFormats.formatToHour(headerTime)
            }
        }
    }

    fun setEvents(events: List<Event>) {
        items.clear()
        events.mapTo(items) {
            ScheduleListItem.Event(it)
        }
        events.forEach {
            val header = ScheduleListItem.HourHeader(it.startDate.toLocalTime())
            if (!items.contains(header)) {
                items.add(header)
            }
        }
        items.sortWith(ScheduleListItem.Comparator)
        notifyDataSetChanged()
    }
}