package isel.ps.ps_userclient.presentations

import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.support.v4.content.LocalBroadcastManager
import isel.ps.ps_userclient.App
import isel.ps.ps_userclient.R
import isel.ps.ps_userclient.models.parcelables.mService
import isel.ps.ps_userclient.models.parcelables.mServiceWrapper
import isel.ps.ps_userclient.receivers.NetworkReceiver
import isel.ps.ps_userclient.services.NetworkService
import isel.ps.ps_userclient.utils.adapters.SubscriptionAdapter
import isel.ps.ps_userclient.utils.constants.IntentKeys
import isel.ps.ps_userclient.utils.constants.ServiceActions
import isel.ps.ps_userclient.utils.services.ServiceUtils
import kotlinx.android.synthetic.main.activity_subscription.*

class SubscriptionActivity : BaseActivity() {

    override val layoutResId: Int = R.layout.activity_subscription
    override val actionBarId: Int? = R.id.toolbar
    override val actionBarMenuResId: Int? = R.menu.main_menu

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val service_info = intent?.extras?.getParcelable<mServiceWrapper>(IntentKeys.SERVICE_INFO)
        subscriptionsView.adapter=SubscriptionAdapter((application as App),this, service_info?.services as ArrayList<mService>)

        text_subscription_page.text = service_info.curr_page.toString()

        btn_subs_next.isEnabled = ServiceUtils.checkLinksHasRelField(service_info._links,"next")
        btn_subs_next.setOnClickListener {
            val new_intent = Intent(this, NetworkService::class.java)
            new_intent.putExtra(IntentKeys.ACTION, ServiceActions.GET_USER_SUBSCRIPTIONS)
            new_intent.putExtra(IntentKeys.PAGE_REQUEST, service_info.curr_page +1)
            startService(new_intent)

        }
        btn_subs_prev.isEnabled = ServiceUtils.checkLinksHasRelField(service_info._links,"prev")
        btn_subs_prev.setOnClickListener {
            val new_intent = Intent(this, NetworkService::class.java)
            new_intent.putExtra(IntentKeys.ACTION, ServiceActions.GET_USER_SUBSCRIPTIONS)
            new_intent.putExtra(IntentKeys.PAGE_REQUEST, service_info.curr_page -1)
            startService(new_intent)
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
