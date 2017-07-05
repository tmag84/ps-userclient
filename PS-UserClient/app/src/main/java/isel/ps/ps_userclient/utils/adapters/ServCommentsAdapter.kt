package isel.ps.ps_userclient.utils.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import isel.ps.ps_userclient.App
import isel.ps.ps_userclient.R
import isel.ps.ps_userclient.models.parcelables.Ranking

class ServCommentsAdapter(val app: App, val context: Context, val list: ArrayList<Ranking>) : BaseAdapter() {
    val inflater : LayoutInflater = LayoutInflater.from(context)

    override fun getCount(): Int {
        return list.count()
    }

    override fun getItem(position: Int): Ranking {
        return list[position]
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView
        val mViewHolder: MyViewHolder

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.service_comment_block, parent, false)
            mViewHolder = MyViewHolder(convertView)
            convertView!!.tag = mViewHolder
        } else {
            mViewHolder = convertView.tag as MyViewHolder
        }

        val currentListData = getItem(position)

        mViewHolder.commentUsername.text = currentListData.user_name
        mViewHolder.commentGrade.text = currentListData.value.toString()
        mViewHolder.commentText.text = currentListData.text
        return convertView
    }

    private inner class MyViewHolder(item: View) {
        internal var commentUsername: TextView = item.findViewById(R.id.block_comment_username) as TextView
        internal var commentGrade: TextView = item.findViewById(R.id.block_comment_grade) as TextView
        internal var commentText: TextView = item.findViewById(R.id.block_comment_text) as TextView
    }
}
