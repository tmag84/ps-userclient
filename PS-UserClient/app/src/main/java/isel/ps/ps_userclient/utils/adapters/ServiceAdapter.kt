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
import isel.ps.ps_userclient.models.ListServices
import isel.ps.ps_userclient.services.NetworkService
import isel.ps.ps_userclient.utils.constants.IntentKeys
import isel.ps.ps_userclient.utils.constants.ServiceActions

@Suppress("NAME_SHADOWING")
class ServiceAdapter(val app: App, val context: Context, val list: ArrayList<ListServices>) : BaseAdapter() {
    val inflater : LayoutInflater = LayoutInflater.from(context)

    override fun getCount(): Int {
        return list.count()
    }

    override fun getItem(position: Int): ListServices {
        return list[position]
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView
        val mViewHolder: MyViewHolder

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.service_block, parent, false)
            mViewHolder = MyViewHolder(convertView)
            convertView!!.tag = mViewHolder
        } else {
            mViewHolder = convertView.tag as MyViewHolder
        }

        val currentListData = getItem(position)

        mViewHolder.serviceTitle.text = currentListData.service_name
        mViewHolder.serviceRank.text = currentListData.avg_rank.toString()
        mViewHolder.serviceSubscribers.text = currentListData.n_subscribers.toString()

        val intent_request = Intent(context, NetworkService::class.java)
        if (currentListData.subscribed) {
            mViewHolder.serviceSubscription.text = context.getString(R.string.service_unsubscribe)
            intent_request.putExtra(IntentKeys.ACTION, ServiceActions.UNSUBSCRIBE)
        }
        else {
            mViewHolder.serviceSubscription.text = context.getString(R.string.service_subscribe)
            intent_request.putExtra(IntentKeys.ACTION, ServiceActions.SUBSCRIBE)
        }
        mViewHolder.serviceSubscription.setOnClickListener {
            val intent_request = Intent(context, NetworkService::class.java)
            intent_request.putExtra(IntentKeys.SERVICE_ID, currentListData.service_id)
            context.startActivity(intent_request)
        }
        return convertView
    }

    private inner class MyViewHolder(item: View) {
        internal var serviceTitle: TextView = item.findViewById(R.id.service_name) as TextView
        internal var serviceRank: TextView = item.findViewById(R.id.service_rank) as TextView
        internal var serviceSubscribers: TextView = item.findViewById(R.id.service_subscribers) as TextView
        internal var serviceSubscription: TextView = item.findViewById(R.id.btn_service_subscribe) as Button
    }
}
