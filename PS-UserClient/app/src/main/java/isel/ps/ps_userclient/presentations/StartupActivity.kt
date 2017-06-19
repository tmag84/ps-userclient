package isel.ps.ps_userclient.presentations

import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.support.v4.content.LocalBroadcastManager
import isel.ps.ps_userclient.App

import isel.ps.ps_userclient.R
import isel.ps.ps_userclient.receivers.NetworkReceiver
import isel.ps.ps_userclient.services.NetworkService
import isel.ps.ps_userclient.utils.constants.IntentKeys
import isel.ps.ps_userclient.utils.constants.ServiceActions
import isel.ps.ps_userclient.utils.constants.SharedPreferencesKeys
import kotlinx.android.synthetic.main.activity_startup.*

class StartupActivity : BaseActivity() {

    override val layoutResId: Int = R.layout.activity_startup

    private lateinit var myReceiver : NetworkReceiver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        myReceiver = NetworkReceiver()
        LocalBroadcastManager.getInstance(this).registerReceiver(myReceiver, IntentFilter(IntentKeys.NETWORK_RECEIVER))

        val intent_request = Intent(this, NetworkService::class.java)
        intent_request.putExtra(IntentKeys.ACTION, ServiceActions.GET_USER_SUBSCRIPTIONS)

        (application as App).let {
            val email = it.SHARED_PREFS.getString(SharedPreferencesKeys.USER_EMAIL,"")
            intent_request.putExtra(IntentKeys.USER_EMAIL,email)
            startup_useremail.text = "Loading user ${email}"
        }

        startService(intent_request)
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
