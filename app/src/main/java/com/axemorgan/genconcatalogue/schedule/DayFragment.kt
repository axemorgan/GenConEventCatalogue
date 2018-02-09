package com.axemorgan.genconcatalogue.schedule

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.Unbinder
import com.axemorgan.genconcatalogue.CatalogueApplication
import com.axemorgan.genconcatalogue.R
import com.axemorgan.genconcatalogue.events.Day
import com.axemorgan.genconcatalogue.events.Event
import javax.inject.Inject

class DayFragment : Fragment(), DayContract.View {

    companion object {
        fun create(day: Day): DayFragment {
            val fragment = DayFragment()
            val bundle = Bundle()
            bundle.putSerializable("DAY", day)
            fragment.arguments = bundle
            return fragment
        }
    }


    @BindView(R.id.day_schedule_recycler)
    lateinit var recycler: RecyclerView

    @Inject
    lateinit var presenter: DayContract.Presenter

    private val adapter: ScheduledEventAdapter = ScheduledEventAdapter()
    private lateinit var unbinder: Unbinder

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        CatalogueApplication.get(context).appComponent.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val view = inflater.inflate(R.layout.fragment_day, container, false)
        unbinder = ButterKnife.bind(this, view)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recycler.layoutManager = LinearLayoutManager(context)
        recycler.adapter = adapter

        presenter.attachView(this)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        unbinder.unbind()
        presenter.detachView()
    }

    override fun showEvents(events: List<Event>) {
        adapter.events = events
    }

}

