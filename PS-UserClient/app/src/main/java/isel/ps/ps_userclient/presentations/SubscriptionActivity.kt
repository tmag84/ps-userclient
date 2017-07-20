package isel.ps.ps_userclient.presentations

import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.support.v4.content.LocalBroadcastManager
import android.view.View
import isel.ps.ps_userclient.App
import isel.ps.ps_userclient.R
import isel.ps.ps_userclient.models.mService
import isel.ps.ps_userclient.models.mServiceWrapper
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
        myReceiver = NetworkReceiver(this)
        LocalBroadcastManager.getInstance(this).registerReceiver(myReceiver, IntentFilter(IntentKeys.NETWORK_RECEIVER))

        val service_info = intent?.extras?.getParcelable<mServiceWrapper>(IntentKeys.SERVICE_INFO)
        if (service_info==null) {
            val intent_error = Intent(this,ErrorActivity::class.java)
            intent_error.putExtra(IntentKeys.ERROR,"Problems communication with server")
            startActivity(intent_error)
        }

        subscriptionsView.adapter=SubscriptionAdapter((application as App),this, service_info?.services as ArrayList<mService>)

        text_subscription_page.text = service_info.curr_page.toString()

        if (ServiceUtils.checkLinksHasRelField(service_info._links,"next")) {
            btn_subs_next.isEnabled = true
            btn_subs_next.visibility = View.VISIBLE
        }
        else {
            btn_subs_next.isEnabled = false
            btn_subs_next.visibility = View.GONE
        }
        btn_subs_next.setOnClickListener {
            val new_intent = Intent(this, NetworkService::class.java)
            new_intent.putExtra(IntentKeys.ACTION, ServiceActions.GET_USER_SUBSCRIPTIONS)
            new_intent.putExtra(IntentKeys.PAGE_REQUEST, service_info.curr_page +1)
            startService(new_intent)

        }

        if (service_info.curr_page!=1 || ServiceUtils.checkLinksHasRelField(service_info._links,"prev")) {
            btn_subs_prev.isEnabled = true
            btn_subs_prev.visibility = View.VISIBLE
        }
        else {
            btn_subs_prev.isEnabled = false
            btn_subs_prev.visibility = View.GONE
        }
        btn_subs_prev.setOnClickListener {
            val new_intent = Intent(this, NetworkService::class.java)
            new_intent.putExtra(IntentKeys.ACTION, ServiceActions.GET_USER_SUBSCRIPTIONS)
            new_intent.putExtra(IntentKeys.PAGE_REQUEST, service_info.curr_page -1)
            startService(new_intent)
        }


        btn_subs_sort_subscribers.setOnClickListener{
            val new_intent = Intent(this, NetworkService::class.java)
            new_intent.putExtra(IntentKeys.ACTION, ServiceActions.GET_USER_SUBSCRIPTIONS)
            new_intent.putExtra(IntentKeys.PAGE_REQUEST, service_info.curr_page)
            new_intent.putExtra(IntentKeys.SORT_ORDER, getString(R.string.sort_order_by_subscriptions))
            startService(new_intent)
        }

        btn_subs_sort_ranking.setOnClickListener {
            val new_intent = Intent(this, NetworkService::class.java)
            new_intent.putExtra(IntentKeys.ACTION, ServiceActions.GET_USER_SUBSCRIPTIONS)
            new_intent.putExtra(IntentKeys.PAGE_REQUEST, service_info.curr_page)
            new_intent.putExtra(IntentKeys.SORT_ORDER, getString(R.string.sort_order_by_ranking))
            startService(new_intent)
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
