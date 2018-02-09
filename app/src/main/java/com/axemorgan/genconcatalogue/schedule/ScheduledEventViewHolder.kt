package com.axemorgan.genconcatalogue.schedule

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import com.axemorgan.genconcatalogue.R

class ScheduledEventViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    @BindView(R.id.schedule_event_item_title)
    lateinit var title: TextView
    @BindView(R.id.schedule_event_item_location)
    lateinit var location: TextView
    @BindView(R.id.schedule_event_item_duration)
    lateinit var duration: TextView

    init {
        ButterKnife.bind(this, itemView)
    }

}