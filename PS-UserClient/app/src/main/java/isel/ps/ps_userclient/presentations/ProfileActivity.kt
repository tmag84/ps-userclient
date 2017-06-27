package isel.ps.ps_userclient.presentations

import android.os.Bundle
import android.support.v4.content.LocalBroadcastManager
import isel.ps.ps_userclient.R
import isel.ps.ps_userclient.receivers.NetworkReceiver

class ProfileActivity : BaseActivity() {

    override val layoutResId: Int = R.layout.activity_profile
    override val actionBarId: Int? = R.id.toolbar
    override val actionBarMenuResId: Int? = R.menu.main_menu

    private lateinit var myReceiver : NetworkReceiver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        myReceiver = NetworkReceiver()
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
