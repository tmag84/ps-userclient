package isel.ps.ps_userclient.presentations

import android.app.Dialog
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.support.v4.content.LocalBroadcastManager
import android.widget.Button
import android.widget.RatingBar
import android.widget.TextView
import isel.ps.ps_userclient.App
import isel.ps.ps_userclient.R
import isel.ps.ps_userclient.models.Notice
import isel.ps.ps_userclient.models.Ranking
import isel.ps.ps_userclient.models.mService
import isel.ps.ps_userclient.receivers.NetworkReceiver
import isel.ps.ps_userclient.services.NetworkService
import isel.ps.ps_userclient.utils.adapters.AdaptersUtils
import isel.ps.ps_userclient.utils.adapters.ServCommentsAdapter
import isel.ps.ps_userclient.utils.adapters.ServEventsAdapter
import isel.ps.ps_userclient.utils.adapters.ServNoticesAdapter
import isel.ps.ps_userclient.utils.constants.IntentKeys
import isel.ps.ps_userclient.utils.constants.ServiceActions
import isel.ps.ps_userclient.utils.constants.SharedPreferencesKeys
import kotlinx.android.synthetic.main.activity_service.*
import kotlinx.android.synthetic.main.service_info_block.*
import kotlinx.android.synthetic.main.service_listview.*

class ServiceActivity : BaseActivity() {

    override val layoutResId: Int = R.layout.activity_service
    override val actionBarId: Int? = R.id.toolbar
    override val actionBarMenuResId: Int? = R.menu.main_menu

    private fun fillServiceInfo(service: mService) {
        block_info_type.text = (application as App).serviceTypes.getServiceTypeName(service.service_type)
        block_info_subscribers.text = service.n_subscribers.toString()
        block_info_grade.text = service.avg_rank.toString()
        block_info_contact_email.text = service.provider_email
        block_info_contact_location.text = service.contact_location
        block_info_contact_name.text = service.contact_name
        block_info_number.text = service.contact_number.toString()
    }

    private fun hasUserPosted(service: mService) : Boolean {
        val email = (application as App).SHARED_PREFS.getString(SharedPreferencesKeys.USER_EMAIL,"")

        service.service_rankings.forEach {
            if (it.user_email==email) {
                return true
            }
        }
        return false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        myReceiver = NetworkReceiver(this)
        LocalBroadcastManager.getInstance(this).registerReceiver(myReceiver, IntentFilter(IntentKeys.NETWORK_RECEIVER))

        val service = intent.extras.getParcelable<mService>(IntentKeys.SERVICE)
        if (service==null) {
            val intent_error = Intent(this,ErrorActivity::class.java)
            intent_error.putExtra(IntentKeys.ERROR,"Problems communication with server")
            startActivity(intent_error)
        }

        title_service_title.text = service.name
        text_service_description.text = service.description
        if (service.subscribed) {
            btn_subscribe_service.text = getString(R.string.service_unsubscribe)
        }
        else {
            btn_subscribe_service.text = getString(R.string.service_subscribe)
        }

        service_view_flipper.displayedChild = 0
        fillServiceInfo(service)

        if (hasUserPosted(service)) {
            btn_service_post_rank.text = getString(R.string.btn_alter_comment)
        }
        else {
            btn_service_post_rank.text = getString(R.string.btn_post_comment)
        }

        btn_service_post_rank.setOnClickListener {
            val dialog = Dialog(this)
            dialog.setTitle("Comentário")
            dialog.setContentView(R.layout.dialog_ranking)

            val dialog_post_title = dialog.findViewById(R.id.dialog_post_title) as TextView
            val dialog_btn_submit = dialog.findViewById(R.id.dialog_post_btn_submit) as Button
            val dialog_btn_cancel = dialog.findViewById(R.id.dialog_post_btn_cancel) as Button
            val dialog_test = dialog.findViewById(R.id.dialog_post_text) as TextView
            val dialog_rating = dialog.findViewById(R.id.dialog_rank_rating) as RatingBar

            if (hasUserPosted(service)) {
                dialog_post_title.text = getString(R.string.title_put_rank)
            }
            else {
                dialog_post_title.text = getString(R.string.title_post_rank)
            }

            dialog_btn_submit.setOnClickListener {
                val intent_req = Intent(this,NetworkService::class.java)
                val text = dialog_test.text.toString()
                val value = dialog_rating.rating.toDouble()
                intent_req.putExtra(IntentKeys.ACTION,ServiceActions.POST_RANKING)
                intent_req.putExtra(IntentKeys.SERVICE_ID,service.id)
                intent_req.putExtra(IntentKeys.RANKING_TEXT,text)
                intent_req.putExtra(IntentKeys.RANKING_VALUE,value)
                startService(intent_req)
                dialog.dismiss()
            }
            dialog_btn_cancel.setOnClickListener {
                dialog.dismiss()
            }
            dialog.show()
        }

        btn_subscribe_service.setOnClickListener {
            val intent_subscribe = Intent(this,NetworkService::class.java)
            intent_subscribe.putExtra(IntentKeys.SERVICE_ID,service.id)
            if (service.subscribed) {
                btn_subscribe_service.text = getString(R.string.service_subscribe)
                service.n_subscribers--
                service.subscribed = false
                intent_subscribe.putExtra(IntentKeys.ACTION,ServiceActions.UNSUBSCRIBE)
            }
            else {
                btn_subscribe_service.text = getString(R.string.service_unsubscribe)
                service.n_subscribers++
                service.subscribed = true
                intent_subscribe.putExtra(IntentKeys.ACTION,ServiceActions.SUBSCRIBE)
            }
            block_info_subscribers.text = service.n_subscribers.toString()
            startService(intent_subscribe)
        }

        btn_service_info.setOnClickListener {
            service_view_flipper.displayedChild = 0
            fillServiceInfo(service)
        }

        btn_service_events.setOnClickListener {
            service_view_flipper.displayedChild = 1
            service_multview.adapter = ServEventsAdapter((application as App),this, AdaptersUtils.setListEvents(service,service.service_events))
        }

        btn_service_notices.setOnClickListener {
            service_view_flipper.displayedChild = 1
            service_multview.adapter = ServNoticesAdapter((application as App),this, service.service_notices as ArrayList<Notice>)
        }

        btn_service_rankings.setOnClickListener {
            service_view_flipper.displayedChild = 1
            service_multview.adapter = ServCommentsAdapter((application as App),this, service.service_rankings as ArrayList<Ranking>)
        }
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
        LocalBroadcastManager.getInstance(this).unregisterReceiver(myReceiver)
    }

    override fun onDestroy() {
        super.onDestroy()
        LocalBroadcastManager.getInstance(this).unregisterReceiver(myReceiver)
    }
}