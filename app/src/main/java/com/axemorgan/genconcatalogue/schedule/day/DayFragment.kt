package com.axemorgan.genconcatalogue.schedule.day

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
import com.axemorgan.genconcatalogue.event_detail.EventDetailActivity
import com.axemorgan.genconcatalogue.events.Day
import com.axemorgan.genconcatalogue.events.Event
import javax.inject.Inject

class DayFragment : Fragment(), DayContract.View {

    companion object {
        const val ARG_DAY = "DAY"

        fun create(day: Day): DayFragment {
            val fragment = DayFragment()
            val bundle = Bundle()
            bundle.putSerializable(ARG_DAY, day)
            fragment.arguments = bundle
            return fragment
        }
    }


    @BindView(R.id.day_schedule_recycler)
    lateinit var recycler: RecyclerView

    @BindView(R.id.day_schedule_loading)
    lateinit var loadingView: View

    @BindView(R.id.day_schedule_empty)
    lateinit var emptyView: View

    @Inject
    lateinit var presenter: DayContract.Presenter

    private val adapter: ScheduledEventAdapter = ScheduledEventAdapter({ startActivity(EventDetailActivity.forEvent(context, it.id)) })
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

    override fun getDay(): Day {
        return arguments?.getSerializable(ARG_DAY) as Day
    }

    override fun showEvents(events: List<Event>) {
        adapter.setEvents(events)
        loadingView.visibility = View.GONE
        recycler.visibility = View.VISIBLE
        emptyView.visibility = View.GONE
    }

    override fun showEmpty() {
        recycler.visibility = View.GONE
        loadingView.visibility = View.GONE
        emptyView.visibility = View.VISIBLE
    }


    override fun showLoading() {
        loadingView.visibility = View.VISIBLE
        recycler.visibility = View.GONE
        emptyView.visibility = View.GONE
    }
}

