package com.axemorgan.genconcatalogue.schedule

import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import butterknife.BindView
import butterknife.ButterKnife
import com.axemorgan.genconcatalogue.CatalogueApplication
import com.axemorgan.genconcatalogue.R
import com.axemorgan.genconcatalogue.components.DateFormats.formatDay
import com.axemorgan.genconcatalogue.events.Day
import com.axemorgan.genconcatalogue.schedule.day.DayFragment
import org.threeten.bp.LocalDateTime
import org.threeten.bp.ZoneOffset
import javax.inject.Inject

class ScheduleFragment : Fragment(), ScheduleContract.View {

    companion object Factory {
        fun create(): ScheduleFragment {
            return ScheduleFragment()
        }
    }

    @Inject
    lateinit var presenter: ScheduleContract.Presenter

    @BindView(R.id.schedule_pager)
    lateinit var pager: ViewPager
    @BindView(R.id.schedule_tabs)
    lateinit var tabs: TabLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        CatalogueApplication.get(context).appComponent.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = layoutInflater.inflate(R.layout.fragment_schedule, container, false)
        view?.let { ButterKnife.bind(this, it) }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        pager.adapter = fragmentManager?.let { SchedulePagerAdapter(it) }
        tabs.setupWithViewPager(pager)
        tabs.addOnTabSelectedListener(TabLayout.ViewPagerOnTabSelectedListener(pager))

        presenter.attachView(this)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        presenter.detachView()
    }
}

class SchedulePagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        return DayFragment.create(Day.values()[position])
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return formatDay(LocalDateTime.ofEpochSecond(Day.values()[position].startTime, 0, ZoneOffset.ofHours(-4)))
    }

    override fun getCount(): Int {
        return Day.values().size
    }
}
