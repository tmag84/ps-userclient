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
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : BaseActivity() {

    override val layoutResId: Int = R.layout.activity_register

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        btn_registration.setOnClickListener {
            val email = text_register_email.text.toString()
            val password = text_register_password.text.toString()
            val confirm_password = text_register_confirm_password.toString()
            val username = text_register_username.toString()

            if (password==confirm_password) {
                (application as App).let{
                    val editor = it.SHARED_PREFS.edit()
                    editor.putString(SharedPreferencesKeys.USER_EMAIL,email)
                    editor.putString(SharedPreferencesKeys.USER_PASSWORD,password)
                    editor.putString(SharedPreferencesKeys.USER_NAME,username)
                    editor.apply()
                }
                val intent_request = Intent(this, NetworkService::class.java)
                intent_request.putExtra(IntentKeys.ACTION, ServiceActions.REGISTER)
                intent_request.putExtra(IntentKeys.USER_EMAIL, email)
                intent_request.putExtra(IntentKeys.USER_PASSWORD, password)
                intent_request.putExtra(IntentKeys.USERNAME, username)
                startService(intent_request)
            }
            else {
                text_register_error.text = getString(R.string.text_compare_passwords)
            }
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
