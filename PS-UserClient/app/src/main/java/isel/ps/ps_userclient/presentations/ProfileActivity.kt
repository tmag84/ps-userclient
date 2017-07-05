package isel.ps.ps_userclient.presentations

import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.support.v4.content.LocalBroadcastManager
import android.text.SpannableStringBuilder
import isel.ps.ps_userclient.App
import isel.ps.ps_userclient.R
import isel.ps.ps_userclient.receivers.NetworkReceiver
import isel.ps.ps_userclient.services.NetworkService
import isel.ps.ps_userclient.utils.constants.IntentKeys
import isel.ps.ps_userclient.utils.constants.ServiceActions
import isel.ps.ps_userclient.utils.constants.SharedPreferencesKeys
import kotlinx.android.synthetic.main.activity_profile.*

class ProfileActivity : BaseActivity() {

    override val layoutResId: Int = R.layout.activity_profile
    override val actionBarId: Int? = R.id.toolbar
    override val actionBarMenuResId: Int? = R.menu.main_menu

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val set = (application as App).SHARED_PREFS.getStringSet(SharedPreferencesKeys.PREFERED_TYPES,HashSet<String>())
        val list = ArrayList<String>(set)

        (application as App).let{
            val username = it.SHARED_PREFS.getString(SharedPreferencesKeys.USER_NAME,"")

            text_profile_username.text = SpannableStringBuilder(username)
            it.serviceTypes.checkBoxes(this,list)

            btn_submit.setOnClickListener {
                val new_list = (application as App).serviceTypes.getPreferencesList(this)
                val editor = (application as App).SHARED_PREFS.edit()
                editor.putStringSet(SharedPreferencesKeys.PREFERED_TYPES,HashSet<String>(new_list))
                editor.apply()

                if (text_profile_username.text.toString()!=username) {
                    val intent_request = Intent(this, NetworkService::class.java)
                    intent_request.putExtra(IntentKeys.ACTION,ServiceActions.USER_INFO_CHANGE)
                    intent_request.putExtra(IntentKeys.USERNAME,text_profile_username.text.toString())
                }
            }

            btn_cancel.setOnClickListener {
                startActivity(Intent(this,ProfileActivity::class.java))
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
