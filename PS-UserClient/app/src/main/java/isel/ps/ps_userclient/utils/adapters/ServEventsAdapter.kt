package isel.ps.ps_userclient.utils.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.TextView
import isel.ps.ps_userclient.App
import isel.ps.ps_userclient.R
import isel.ps.ps_userclient.models.ListEvents
import isel.ps.ps_userclient.utils.async.AsyncData
import isel.ps.ps_userclient.utils.async.CalendarOpAsync
import isel.ps.ps_userclient.utils.async.CalendarQueryAsync
import isel.ps.ps_userclient.utils.dates.DateUtils

class ServEventsAdapter(val app: App, val context: Context, val list: ArrayList<ListEvents>) : BaseAdapter() {
    val inflater : LayoutInflater = LayoutInflater.from(context)

    override fun getCount(): Int {
        return list.count()
    }

    override fun getItem(position: Int): ListEvents {
        return list[position]
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        @Suppress("NAME_SHADOWING")
        var convertView = convertView
        val mViewHolder: MyViewHolder

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.service_events_block, parent, false)
            mViewHolder = MyViewHolder(convertView)
            convertView!!.tag = mViewHolder
        } else {
            mViewHolder = convertView.tag as MyViewHolder
        }

        val currentListData = getItem(position)

        mViewHolder.eventName.text = currentListData.text
        mViewHolder.eventDate.text = DateUtils.unixToDate(currentListData.event_begin)
        mViewHolder.eventDuration.text = DateUtils.getDurationAsString(currentListData.event_end-currentListData.event_begin)

        val data = AsyncData(currentListData,mViewHolder.addToCalendar)
        mViewHolder.addToCalendar.isEnabled = false
        CalendarQueryAsync(context).execute(data)

        mViewHolder.addToCalendar.setOnClickListener {
            CalendarOpAsync(context).execute(data)
        }
        return convertView
    }

    private inner class MyViewHolder(item: View) {
        internal var eventName: TextView = item.findViewById(R.id.block_event_name) as TextView
        internal var eventDate: TextView = item.findViewById(R.id.block_event_date) as TextView
        internal var eventDuration: TextView = item.findViewById(R.id.block_event_duration) as TextView
        internal var addToCalendar: Button = item.findViewById(R.id.btn_block_add_calendar) as Button
    }
}
