package isel.ps.ps_userclient.presentations

import android.app.Dialog
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.support.v4.content.LocalBroadcastManager
import android.text.SpannableStringBuilder
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
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
        myReceiver = NetworkReceiver(this)
        LocalBroadcastManager.getInstance(this).registerReceiver(myReceiver, IntentFilter(IntentKeys.NETWORK_RECEIVER))

        val set = (application as App).SHARED_PREFS.getStringSet(SharedPreferencesKeys.PREFERED_TYPES,HashSet<String>())
        val list = ArrayList<String>(set)

        val username = (application as App).SHARED_PREFS.getString(SharedPreferencesKeys.USER_NAME,"")
        text_profile_username.text = SpannableStringBuilder(username)
        (application as App).serviceTypes.checkBoxes(this,list)
        btn_submit.setOnClickListener {
            val new_list = (application as App).serviceTypes.getPreferencesList(this)
            val editor = (application as App).SHARED_PREFS.edit()
            editor.putStringSet(SharedPreferencesKeys.PREFERED_TYPES,HashSet<String>(new_list))
            editor.apply()
            Toast.makeText(this,"Áreas de preferência gravadas.",Toast.LENGTH_SHORT).show()

            if (text_profile_username.text.toString()!=username) {
                val intent_request = Intent(this, NetworkService::class.java)
                intent_request.putExtra(IntentKeys.ACTION,ServiceActions.USER_INFO_CHANGE)
                intent_request.putExtra(IntentKeys.USERNAME,text_profile_username.text.toString())
                startService(intent_request)
            }
        }

        btn_cancel.setOnClickListener {
            startActivity(Intent(this,ProfileActivity::class.java))
        }

        btn_clear_info.setOnClickListener {
            val editor = (application as App).SHARED_PREFS.edit()
            editor.remove(SharedPreferencesKeys.USER_PASSWORD)
            editor.remove(SharedPreferencesKeys.USER_NAME)
            editor.remove(SharedPreferencesKeys.USER_EMAIL)
            editor.remove(SharedPreferencesKeys.AUTH_TOKEN)
            editor.remove(SharedPreferencesKeys.PREFERED_TYPES)
            editor.remove("initialized")
            editor.apply()
            startActivity(Intent(this,LoginActivity::class.java))
        }

        btn_change_my_password.setOnClickListener {
            val dialog = Dialog(this)
            dialog.setTitle("Alterar Password")
            dialog.setContentView(R.layout.dialog_password_edit)

            val dialog_btn_submit = dialog.findViewById(R.id.btn_change_submit) as Button
            val dialog_btn_cancel = dialog.findViewById(R.id.btn_change_cancel) as Button
            val text_password = dialog.findViewById(R.id.text_change_password) as EditText
            val text_confirm_password = dialog.findViewById(R.id.text_confirm_change_password) as EditText
            val text_error = dialog.findViewById(R.id.text_change_pass_error) as TextView

            dialog_btn_submit.setOnClickListener {
                if (text_password.text==text_confirm_password.text) {
                    val intent_req = Intent(this,NetworkService::class.java)
                    intent_req.putExtra(IntentKeys.ACTION,ServiceActions.PASSWORD_CHANGE)
                    intent_req.putExtra(IntentKeys.USER_PASSWORD,text_password.text.toString())
                    startService(intent_req)
                    dialog.dismiss()
                }
                else {
                    text_error.text = getString(R.string.text_change_pass_error)
                    text_password.text.clear()
                    text_confirm_password.text.clear()
                }
            }
            dialog_btn_cancel.setOnClickListener {
                dialog.dismiss()
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
