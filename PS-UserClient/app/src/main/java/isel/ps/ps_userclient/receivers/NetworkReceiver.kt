package isel.ps.ps_userclient.receivers

import android.app.Activity
import android.content.Intent
import android.content.Context
import android.content.BroadcastReceiver
import isel.ps.ps_userclient.App
import isel.ps.ps_userclient.models.parcelables.mLogin
import isel.ps.ps_userclient.models.parcelables.mServiceWrapper
import isel.ps.ps_userclient.presentations.*
import isel.ps.ps_userclient.services.NetworkService
import isel.ps.ps_userclient.utils.constants.IntentKeys
import isel.ps.ps_userclient.utils.constants.ServiceActions
import isel.ps.ps_userclient.utils.constants.ServiceResponses
import isel.ps.ps_userclient.utils.constants.SharedPreferencesKeys
import isel.ps.ps_userclient.utils.dates.DateUtils
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_register.*

class NetworkReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val serv_response = intent?.extras?.get(IntentKeys.SERVICE_RESPONSE)
        val new_intent : Intent

        when (serv_response) {
            ServiceResponses.SUBSCRIPTIONS_REQUEST_SUCCESS -> {
                val service_info = intent.extras?.getParcelable<mServiceWrapper>(IntentKeys.SERVICE_INFO)
                new_intent = Intent(context, SubscriptionActivity::class.java)
                new_intent.putExtra(IntentKeys.SERVICE_INFO, service_info)
                context?.startActivity(new_intent)
            }
            ServiceResponses.SUBSCRIPTIONS_REQUEST_FAILURE -> {
                val error = intent.extras?.getString(IntentKeys.ERROR)
                new_intent = Intent(context,ErrorActivity::class.java)
                new_intent.putExtra(IntentKeys.ERROR,error)
                context?.startActivity(new_intent)
            }
            ServiceResponses.SEARCH_REQUEST_SUCCESS -> {
                val service_info = intent.extras?.getParcelable<mServiceWrapper>(IntentKeys.SERVICE_INFO)
                val searchWithPreferences = intent.extras?.getBoolean(IntentKeys.SEARCH_BY_PREFERENCE)
                new_intent = Intent(context, SearchActivity::class.java)
                new_intent.putExtra(IntentKeys.SERVICE_INFO, service_info)
                new_intent.putExtra(IntentKeys.SEARCH_BY_PREFERENCE,searchWithPreferences)
                context?.startActivity(new_intent)
            }
            ServiceResponses.SEARCH_REQUEST_FAILURE -> {
                val error = intent.extras?.getString(IntentKeys.ERROR)
                new_intent = Intent(context,ErrorActivity::class.java)
                new_intent.putExtra(IntentKeys.ERROR,error)
                context?.startActivity(new_intent)
            }
            ServiceResponses.LOGIN_SUCCESS -> {
                val login_info = intent.extras?.getParcelable<mLogin>(IntentKeys.LOGIN_INFO)
                val expire_date = DateUtils.addSecondsToCurrentTime(login_info!!.expires_in)

                (context?.applicationContext as App).let {
                    val editor = it.SHARED_PREFS.edit()
                    editor.putString(SharedPreferencesKeys.AUTH_TOKEN,login_info.access_token)
                    editor.putLong(SharedPreferencesKeys.EXPIRE_DATE,expire_date)
                    editor.apply()
                }
                new_intent = Intent(context, NetworkService::class.java)
                new_intent.putExtra(IntentKeys.ACTION,ServiceActions.GET_USER_SUBSCRIPTIONS)
                context.startService(new_intent)
            }
            ServiceResponses.LOGIN_FAILURE -> {
                val error = intent.extras?.getString(IntentKeys.ERROR)

                (context?.applicationContext as App).let {
                    val editor = it.SHARED_PREFS.edit()
                    editor.remove(SharedPreferencesKeys.USER_EMAIL)
                    editor.remove(SharedPreferencesKeys.USER_PASSWORD)
                    editor.commit()
                }

                (context as Activity).let {
                    it.text_login_error.text = error
                    it.text_login_email.text.clear()
                    it.text_login_password.text.clear()
                }
            }
            ServiceResponses.REGISTRATION_SUCCESS -> {
                new_intent = Intent(context, ProfileActivity::class.java)
                context?.startActivity(new_intent)
            }
            ServiceResponses.REGISTRATION_FAILURE -> {
                val error = intent.extras?.getString(IntentKeys.ERROR)

                (context?.applicationContext as App).let {
                    val editor = it.SHARED_PREFS.edit()
                    editor.remove(SharedPreferencesKeys.USER_EMAIL)
                    editor.remove(SharedPreferencesKeys.USER_PASSWORD)
                    editor.remove(SharedPreferencesKeys.USER_NAME)
                    editor.apply()
                }
                (context as Activity).let {
                    it.text_register_error.text = error
                    it.text_register_email.text.clear()
                    it.text_register_password.text.clear()
                    it.text_register_username.text.clear()
                }
            }
            ServiceResponses.SERVICE_REQUEST_SUCCESS -> {
                val service = intent.extras?.getParcelable<mServiceWrapper>(IntentKeys.SERVICE)
                new_intent = Intent(context, ServiceActivity::class.java)
                new_intent.putExtra(IntentKeys.SERVICE_INFO, service)
                context?.startActivity(new_intent)
            }
            ServiceResponses.SERVICE_REQUEST_FAILURE -> {
                val error = intent.extras?.getString(IntentKeys.ERROR)
                new_intent = Intent(context,ErrorActivity::class.java)
                new_intent.putExtra(IntentKeys.ERROR,error)
                context?.startActivity(new_intent)
            }
            ServiceResponses.RANKING_POST_SUCESS -> {

            }
            ServiceResponses.RANKING_POST_FAILURE -> {
                val error = intent.extras?.getString(IntentKeys.ERROR)
                new_intent = Intent(context,ErrorActivity::class.java)
                new_intent.putExtra(IntentKeys.ERROR,error)
                context?.startActivity(new_intent)
            }
            ServiceResponses.SUBSCRIPTION_ACTION_SUCCESS -> {

            }
            ServiceResponses.SUBSCRIPTION_ACTION_FAILURE -> {
                val error = intent.extras?.getString(IntentKeys.ERROR)
                new_intent = Intent(context,ErrorActivity::class.java)
                new_intent.putExtra(IntentKeys.ERROR,error)
                context?.startActivity(new_intent)

            }
            ServiceResponses.NO_CONNECTION -> {
                new_intent = Intent(context,ErrorActivity::class.java)
                new_intent.putExtra(IntentKeys.ERROR,"No Wifi Connection")
                context?.startActivity(new_intent)
            }
        }
    }
}
