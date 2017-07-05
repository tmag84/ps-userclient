package isel.ps.ps_userclient.presentations

import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.support.v4.content.LocalBroadcastManager
import isel.ps.ps_userclient.App
import isel.ps.ps_userclient.R
import isel.ps.ps_userclient.models.parcelables.Event
import isel.ps.ps_userclient.models.parcelables.Notice
import isel.ps.ps_userclient.models.parcelables.Ranking
import isel.ps.ps_userclient.models.parcelables.mService
import isel.ps.ps_userclient.receivers.NetworkReceiver
import isel.ps.ps_userclient.services.NetworkService
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

    private fun fillServiceInfo(service:mService) {
        block_info_type.text = (application as App).serviceTypes.getServiceTypeName(service.service_type)
        block_info_subscribers.text = service.n_subscribers.toString()
        block_info_grade.text = service.avg_rank.toString()
        block_info_contact_email.text = service.provider_email
        block_info_contact_location.text = service.contact_location
        block_info_contact_name.text = service.contact_name
        block_info_number.text = service.contact_number.toString()
    }

    private fun hasUserPosted(service:mService) : Boolean {
        val email = "tmagalhaes84@gmail.com"
        //val email = it.SHARED_PREFS.getString(SharedPreferencesKeys.USER_EMAIL,"")

        service.service_rankings.forEach {
            if (it.user_email==email) {
                return true
            }
        }
        return false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val service = intent.extras.getParcelable<mService>(IntentKeys.SERVICE)

        title_service_title.text = service.name
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

            //

        }

        btn_subscribe_service.setOnClickListener {
            val intent_subscribe = Intent(this,NetworkService::class.java)
            intent_subscribe.putExtra(IntentKeys.SERVICE_ID,service.id)
            if (service.subscribed) {
                intent_subscribe.putExtra(IntentKeys.ACTION,ServiceActions.UNSUBSCRIBE)
            }
            else {
                intent_subscribe.putExtra(IntentKeys.ACTION,ServiceActions.SUBSCRIBE)
            }
            startService(intent_subscribe)
        }



        btn_service_info.setOnClickListener {
            service_view_flipper.displayedChild = 0
            fillServiceInfo(service)
        }

        btn_service_events.setOnClickListener {
            service_view_flipper.displayedChild = 1
            service_multview.adapter = ServEventsAdapter((application as App),this, service.service_events as ArrayList<Event>)
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
        myReceiver = NetworkReceiver()
        LocalBroadcastManager.getInstance(this).registerReceiver(myReceiver, IntentFilter(IntentKeys.NETWORK_RECEIVER))
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
