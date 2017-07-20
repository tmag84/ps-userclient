package isel.ps.ps_userclient.presentations

import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.support.v4.content.LocalBroadcastManager
import android.view.View
import isel.ps.ps_userclient.App
import isel.ps.ps_userclient.R
import isel.ps.ps_userclient.models.mUserEventWrapper
import isel.ps.ps_userclient.receivers.NetworkReceiver
import isel.ps.ps_userclient.services.NetworkService
import isel.ps.ps_userclient.utils.adapters.AdaptersUtils
import isel.ps.ps_userclient.utils.adapters.EventsAdapter
import isel.ps.ps_userclient.utils.constants.IntentKeys
import isel.ps.ps_userclient.utils.constants.ServiceActions
import isel.ps.ps_userclient.utils.services.ServiceUtils
import kotlinx.android.synthetic.main.activity_event.*

class EventActivity : BaseActivity() {

    override val layoutResId: Int = R.layout.activity_event
    override val actionBarId: Int? = R.id.toolbar
    override val actionBarMenuResId: Int? = R.menu.main_menu

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        myReceiver = NetworkReceiver(this)
        LocalBroadcastManager.getInstance(this).registerReceiver(myReceiver, IntentFilter(IntentKeys.NETWORK_RECEIVER))

        val events_info = intent.getParcelableExtra<mUserEventWrapper>(IntentKeys.EVENTS_INFO)
        if (events_info==null) {
            val intent_error = Intent(this,ErrorActivity::class.java)
            intent_error.putExtra(IntentKeys.ERROR,"Problems communication with server")
            startActivity(intent_error)
        }

        if (events_info.curr_page!=1 || ServiceUtils.checkLinksHasRelField(events_info._links,"prev")) {
            btn_event_prev.isEnabled = true
            btn_event_prev.visibility = View.VISIBLE
        }
        else {
            btn_event_prev.isEnabled = false
            btn_event_prev.visibility = View.GONE
        }
        btn_event_prev.setOnClickListener {
            val new_intent = Intent(this, NetworkService::class.java)
            new_intent.putExtra(IntentKeys.ACTION, ServiceActions.GET_USER_EVENTS)
            new_intent.putExtra(IntentKeys.PAGE_REQUEST, events_info?.curr_page!! -1)
            startService(new_intent)
        }

        if (ServiceUtils.checkLinksHasRelField(events_info._links,"next")) {
            btn_event_next.isEnabled = true
            btn_event_next.visibility = View.VISIBLE
        }
        else {
            btn_event_next.isEnabled = false
            btn_event_next.visibility = View.GONE
        }
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
