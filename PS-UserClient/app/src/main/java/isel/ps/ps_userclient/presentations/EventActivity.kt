package isel.ps.ps_userclient.presentations

import android.content.IntentFilter
import android.os.Bundle
import android.support.v4.content.LocalBroadcastManager

import isel.ps.ps_userclient.R
import isel.ps.ps_userclient.receivers.NetworkReceiver
import isel.ps.ps_userclient.utils.constants.IntentKeys

class EventActivity : BaseActivity() {

    override val layoutResId: Int = R.layout.activity_login
    override val actionBarId: Int? = R.id.toolbar
    override val actionBarMenuResId: Int? = R.menu.main_menu

    private lateinit var myReceiver : NetworkReceiver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        myReceiver = NetworkReceiver()

        LocalBroadcastManager.getInstance(this).registerReceiver(myReceiver, IntentFilter(IntentKeys.NETWORK_RECEIVER))
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
