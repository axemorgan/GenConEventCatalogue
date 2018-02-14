package com.axemorgan.genconcatalogue.schedule.day

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import com.axemorgan.genconcatalogue.R

class ScheduleHeaderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    @BindView(R.id.schedule_hour_header_time)
    lateinit var time: TextView

    init {
        ButterKnife.bind(this, itemView)
    }
}