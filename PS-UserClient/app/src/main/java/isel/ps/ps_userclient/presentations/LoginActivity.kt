package isel.ps.ps_userclient.presentations

import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.support.v4.content.LocalBroadcastManager

import isel.ps.ps_userclient.R
import isel.ps.ps_userclient.receivers.NetworkReceiver
import isel.ps.ps_userclient.services.NetworkService
import isel.ps.ps_userclient.utils.constants.IntentKeys
import isel.ps.ps_userclient.utils.constants.ServiceActions
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : BaseActivity() {

    override val layoutResId: Int = R.layout.activity_login
    override val actionBarId: Int? = R.id.toolbar
    override val actionBarMenuResId: Int? = R.menu.main_menu

    private lateinit var myReceiver : NetworkReceiver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        myReceiver = NetworkReceiver()

        LocalBroadcastManager.getInstance(this).registerReceiver(myReceiver, IntentFilter(IntentKeys.NETWORK_RECEIVER))

        btn_login.setOnClickListener {
            val email = text_login_email.text.toString()
            val password = text_login_password.text.toString()

            val intent_request = Intent(this, NetworkService::class.java)
            intent_request.putExtra(IntentKeys.ACTION, ServiceActions.LOGIN)
            intent_request.putExtra(IntentKeys.USER_EMAIL, email)
            intent_request.putExtra(IntentKeys.USER_PASSWORD, password)
            startService(intent_request)
        }

        btn_register.setOnClickListener {
            startActivity(Intent(this,RegisterActivity::class.java))
        }
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