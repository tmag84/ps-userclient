package isel.ps.ps_userclient.utils.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import isel.ps.ps_userclient.App
import isel.ps.ps_userclient.R
import isel.ps.ps_userclient.models.Notice
import isel.ps.ps_userclient.utils.dates.DateUtils

class ServNoticesAdapter(val app: App, val context: Context, val list: ArrayList<Notice>) : BaseAdapter() {
    val inflater : LayoutInflater = LayoutInflater.from(context)

    override fun getCount(): Int {
        return list.count()
    }

    override fun getItem(position: Int): Notice {
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
            convertView = inflater.inflate(R.layout.service_notices_block, parent, false)
            mViewHolder = MyViewHolder(convertView)
            convertView!!.tag = mViewHolder
        } else {
            mViewHolder = convertView.tag as MyViewHolder
        }

        val currentListData = getItem(position)

        mViewHolder.noticeDate.text = DateUtils.unixToDate(currentListData.creation_date)
        mViewHolder.noticeText.text = currentListData.text
        return convertView
    }

    private inner class MyViewHolder(item: View) {
        internal var noticeDate: TextView = item.findViewById(R.id.block_notice_date) as TextView
        internal var noticeText: TextView = item.findViewById(R.id.block_notice_text) as TextView
    }
}
