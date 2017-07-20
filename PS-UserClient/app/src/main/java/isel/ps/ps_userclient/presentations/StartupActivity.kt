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
import isel.ps.ps_userclient.utils.dates.DateUtils
import kotlinx.android.synthetic.main.activity_startup.*

class StartupActivity : BaseActivity() {

    override val layoutResId: Int = R.layout.activity_startup

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        myReceiver = NetworkReceiver(this)
        LocalBroadcastManager.getInstance(this).registerReceiver(myReceiver, IntentFilter(IntentKeys.NETWORK_RECEIVER))

        (application as App).let {
            if (it.SHARED_PREFS.contains(SharedPreferencesKeys.USER_EMAIL)) {
                val intent_request = Intent(this, NetworkService::class.java)

                val email = it.SHARED_PREFS.getString(SharedPreferencesKeys.USER_EMAIL,"")
                val password = it.SHARED_PREFS.getString(SharedPreferencesKeys.USER_PASSWORD,"")
                val username = it.SHARED_PREFS.getString(SharedPreferencesKeys.USER_NAME,"")
                startup_useremail.text = "${getString(R.string.text_loading)} $username"

                if (it.SHARED_PREFS.contains(SharedPreferencesKeys.EXPIRE_DATE) &&
                        DateUtils.isDateAfterCurrentDay(it.SHARED_PREFS.getLong(SharedPreferencesKeys.EXPIRE_DATE,0))) {

                    intent_request.putExtra(IntentKeys.ACTION, ServiceActions.GET_USER_SUBSCRIPTIONS)
                }
                else {
                    val editor = it.SHARED_PREFS.edit()
                    editor.remove(SharedPreferencesKeys.AUTH_TOKEN)
                    editor.remove(SharedPreferencesKeys.EXPIRE_DATE)
                    editor.apply()

                    intent_request.putExtra(IntentKeys.ACTION, ServiceActions.AUTO_LOGIN)
                    intent_request.putExtra(IntentKeys.USER_EMAIL,email)
                    intent_request.putExtra(IntentKeys.USER_PASSWORD,password)
                }

                startService(intent_request)
            }
            else {
                startActivity(Intent(this,LoginActivity::class.java))
            }
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