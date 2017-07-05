package isel.ps.ps_userclient.utils.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.TextView
import isel.ps.ps_userclient.App
import isel.ps.ps_userclient.R
import isel.ps.ps_userclient.models.parcelables.Event
import isel.ps.ps_userclient.services.NetworkService
import isel.ps.ps_userclient.utils.constants.IntentKeys
import isel.ps.ps_userclient.utils.constants.ServiceActions

class ServEventsAdapter(val app: App, val context: Context, val list: ArrayList<Event>) : BaseAdapter() {
    val inflater : LayoutInflater = LayoutInflater.from(context)

    override fun getCount(): Int {
        return list.count()
    }

    override fun getItem(position: Int): Event {
        return list[position]
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
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
        mViewHolder.eventDate.text = currentListData.event_date
        mViewHolder.addToCalendar.setOnClickListener {
            val intent_request = Intent(context, NetworkService::class.java)
            intent_request.putExtra(IntentKeys.ACTION, ServiceActions.ADD_EVENT_TO_CALENDAR)
            intent_request.putExtra(IntentKeys.EVENT_DATE, currentListData.event_date)
            context.startActivity(intent_request)
        }
        return convertView
    }

    private inner class MyViewHolder(item: View) {
        internal var eventName: TextView = item.findViewById(R.id.block_event_name) as TextView
        internal var eventDate: TextView = item.findViewById(R.id.block_event_date) as TextView
        internal var addToCalendar: Button = item.findViewById(R.id.btn_block_add_calendar) as Button
    }
}
