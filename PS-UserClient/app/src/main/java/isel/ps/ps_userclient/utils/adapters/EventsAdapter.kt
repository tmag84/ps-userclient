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
import isel.ps.ps_userclient.models.ListEvents
import isel.ps.ps_userclient.services.NetworkService

class EventsAdapter(val app: App, val context: Context, val list: ArrayList<ListEvents>) : BaseAdapter() {
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
        var convertView = convertView
        val mViewHolder: MyViewHolder

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.event_block, parent, false)
            mViewHolder = MyViewHolder(convertView)
            convertView!!.tag = mViewHolder
        } else {
            mViewHolder = convertView.tag as MyViewHolder
        }

        val currentListData = getItem(position)

        val intent_request = Intent(context, NetworkService::class.java)

        //mViewHolder.serviceTitle.text = currentListData.service_name
        /*mViewHolder.serviceTitle.setOnClickListener {
            intent_request.putExtra(IntentKeys.ACTION, ServiceActions.GET_SERVICE)
            intent_request.putExtra(IntentKeys.SERVICE_ID,currentListData.service_id)
            context.startService(intent_request)
        }*/

        //mViewHolder.serviceType.text = currentListData.service_type.toString()
        //mViewHolder.serviceRank.text = currentListData.avg_rank.toString()
        //mViewHolder.serviceSubscribers.text = currentListData.n_subscribers.toString()

        /*
        mViewHolder.serviceSubscription.setOnClickListener {
            val intent_request = Intent(context, NetworkService::class.java)
            intent_request.putExtra(IntentKeys.SERVICE_ID, currentListData.service_id)
            context.startActivity(intent_request)
        }*/
        return convertView
    }

    private inner class MyViewHolder(item: View) {
        //internal var serviceTitle: TextView = item.findViewById(R.id.service_name) as TextView
        //internal var serviceType: TextView = item.findViewById(R.id.service_type) as TextView
        //internal var serviceRank: TextView = item.findViewById(R.id.service_rank) as TextView
        //internal var serviceSubscribers: TextView = item.findViewById(R.id.service_subscribers) as TextView
        //internal var serviceSubscription: Button = item.findViewById(R.id.btn_service_subscribe) as Button
    }
}