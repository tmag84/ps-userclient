package isel.ps.ps_userclient.presentations

import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.support.v4.content.LocalBroadcastManager
import isel.ps.ps_userclient.R
import isel.ps.ps_userclient.receivers.NetworkReceiver
import isel.ps.ps_userclient.services.NetworkService
import isel.ps.ps_userclient.utils.constants.ErrorMessages
import isel.ps.ps_userclient.utils.constants.IntentKeys
import isel.ps.ps_userclient.utils.constants.ServiceActions
import kotlinx.android.synthetic.main.activity_error.*

class ErrorActivity : BaseActivity() {

    override val layoutResId: Int = R.layout.activity_error

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        myReceiver = NetworkReceiver(this)
        LocalBroadcastManager.getInstance(this).registerReceiver(myReceiver, IntentFilter(IntentKeys.NETWORK_RECEIVER))

        var error = intent.extras?.getString(IntentKeys.ERROR)
        if (error==null) {
            error = ErrorMessages.CONNECTION_ERROR
        }

        error_text_error.text = error

        val back_to_login = error==ErrorMessages.NO_WIFI_CONNECTION
                || error==ErrorMessages.LOGIN_ERROR
                || error==ErrorMessages.CONNECTION_ERROR
                || error==ErrorMessages.AUTH_ERROR

        if (back_to_login) {
            btn_error_back.text = getString(R.string.btn_error_back_to_login)
        }
        else {
            btn_error_back.text = getString(R.string.btn_error_back_to_subscriber)
        }

        btn_error_back.setOnClickListener {
            if (back_to_login) {
                startActivity(Intent(this,LoginActivity::class.java))
            }
            else {
                val request_intent = Intent(this, NetworkService::class.java)
                request_intent.putExtra(IntentKeys.ACTION, ServiceActions.GET_USER_SUBSCRIPTIONS)
                startService(request_intent)
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
