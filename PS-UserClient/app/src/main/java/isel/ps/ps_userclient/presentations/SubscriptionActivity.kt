package isel.ps.ps_userclient.presentations

import android.content.IntentFilter
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.content.LocalBroadcastManager
import isel.ps.ps_userclient.App

import isel.ps.ps_userclient.R
import isel.ps.ps_userclient.models.ListServices
import isel.ps.ps_userclient.models.parcelables.mService
import isel.ps.ps_userclient.models.parcelables.mServiceWrapper
import isel.ps.ps_userclient.receivers.NetworkReceiver
import isel.ps.ps_userclient.utils.adapters.AdaptersUtils
import isel.ps.ps_userclient.utils.adapters.ServiceAdapter
import isel.ps.ps_userclient.utils.constants.IntentKeys
import kotlinx.android.synthetic.main.activity_subscription.*

class SubscriptionActivity : BaseActivity() {

    override val layoutResId: Int = R.layout.activity_subscription
    override val actionBarId: Int? = R.id.toolbar
    override val actionBarMenuResId: Int? = R.menu.main_menu

    private lateinit var myReceiver : NetworkReceiver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        myReceiver = NetworkReceiver()
        LocalBroadcastManager.getInstance(this).registerReceiver(myReceiver, IntentFilter(IntentKeys.NETWORK_RECEIVER))
        val service_info = intent?.extras?.getParcelable<mServiceWrapper>(IntentKeys.SERVICE_INFO)
        subscriptionsView.adapter=ServiceAdapter((application as App),this, AdaptersUtils.setListServices(service_info?.services))
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        LocalBroadcastManager.getInstance(this).unregisterReceiver(myReceiver)
    }
}
