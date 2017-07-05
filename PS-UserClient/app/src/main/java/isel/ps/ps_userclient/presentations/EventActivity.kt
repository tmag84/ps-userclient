package isel.ps.ps_userclient.presentations

import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.support.v4.content.LocalBroadcastManager
import isel.ps.ps_userclient.App
import isel.ps.ps_userclient.R
import isel.ps.ps_userclient.models.parcelables.mUserEventWrapper
import isel.ps.ps_userclient.receivers.NetworkReceiver
import isel.ps.ps_userclient.services.NetworkService
import isel.ps.ps_userclient.utils.adapters.AdaptersUtils
import isel.ps.ps_userclient.utils.adapters.EventsAdapter
import isel.ps.ps_userclient.utils.constants.IntentKeys
import isel.ps.ps_userclient.utils.constants.ServiceActions
import isel.ps.ps_userclient.utils.services.ServiceUtils
import kotlinx.android.synthetic.main.activity_event.*

class EventActivity : BaseActivity() {

    override val layoutResId: Int = R.layout.activity_login
    override val actionBarId: Int? = R.id.toolbar
    override val actionBarMenuResId: Int? = R.menu.main_menu

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val events_info = intent.getParcelableExtra<mUserEventWrapper>(IntentKeys.EVENTS_INFO)
        btn_event_prev.isEnabled = ServiceUtils.checkLinksHasRelField(events_info?._links,"back")
        btn_event_prev.setOnClickListener {
            val new_intent = Intent(this, NetworkService::class.java)
            new_intent.putExtra(IntentKeys.ACTION, ServiceActions.GET_USER_EVENTS)
            new_intent.putExtra(IntentKeys.PAGE_REQUEST, events_info?.curr_page!! -1)
            startService(new_intent)
        }

        btn_event_next.isEnabled = ServiceUtils.checkLinksHasRelField(events_info?._links,"back")
        btn_event_next.setOnClickListener {
            val new_intent = Intent(this, NetworkService::class.java)
            new_intent.putExtra(IntentKeys.ACTION, ServiceActions.GET_USER_EVENTS)
            new_intent.putExtra(IntentKeys.PAGE_REQUEST, events_info?.curr_page!! -1)
            startService(new_intent)
        }

        eventsView.adapter=EventsAdapter((application as App),this, AdaptersUtils.setUserEvents(events_info.events))
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
