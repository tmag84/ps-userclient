package isel.ps.ps_userclient.receivers

import android.app.Activity
import android.content.Intent
import android.content.Context
import android.content.BroadcastReceiver
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.iid.FirebaseInstanceId
import isel.ps.ps_userclient.App
import isel.ps.ps_userclient.R
import isel.ps.ps_userclient.models.mLogin
import isel.ps.ps_userclient.models.mService
import isel.ps.ps_userclient.models.mServiceWrapper
import isel.ps.ps_userclient.models.mUserEventWrapper
import isel.ps.ps_userclient.presentations.*
import isel.ps.ps_userclient.requests.PostAction
import isel.ps.ps_userclient.services.NetworkService
import isel.ps.ps_userclient.utils.constants.*
import isel.ps.ps_userclient.utils.dates.DateUtils
import org.json.JSONObject

class NetworkReceiver(val activity:Activity) : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val service_response = intent?.extras?.get(IntentKeys.SERVICE_RESPONSE)
        val new_intent : Intent

        when (service_response) {
            ServiceResponses.SUBSCRIPTIONS_REQUEST_SUCCESS -> {
                val service_info = intent.extras?.getParcelable<mServiceWrapper>(IntentKeys.SERVICE_INFO)
                new_intent = Intent(context, SubscriptionActivity::class.java)
                new_intent.putExtra(IntentKeys.SERVICE_INFO, service_info)
                context?.startActivity(new_intent)
            }
            ServiceResponses.SUBSCRIPTIONS_REQUEST_FAILURE -> {
                var error = intent.extras?.getString(IntentKeys.ERROR)
                if (error==null) {
                    error = ErrorMessages.SUBSCRIPTION_REQ_ERROR
                }

                new_intent = Intent(context,ErrorActivity::class.java)
                new_intent.putExtra(IntentKeys.ERROR,error)
                context?.startActivity(new_intent)
            }
            ServiceResponses.SEARCH_REQUEST_SUCCESS -> {
                val service_info = intent.extras?.getParcelable<mServiceWrapper>(IntentKeys.SERVICE_INFO)
                val searchWithPreferences = intent.extras?.getBoolean(IntentKeys.SEARCH_BY_PREFERENCE)
                val type = intent.extras.getInt(IntentKeys.SERVICE_TYPE)
                new_intent = Intent(context, SearchActivity::class.java)
                new_intent.putExtra(IntentKeys.SERVICE_INFO, service_info)
                new_intent.putExtra(IntentKeys.SEARCH_BY_PREFERENCE,searchWithPreferences)
                new_intent.putExtra(IntentKeys.SERVICE_TYPE,type)
                context?.startActivity(new_intent)
            }
            ServiceResponses.SEARCH_REQUEST_FAILURE -> {
                var error = intent.extras?.getString(IntentKeys.ERROR)
                if (error==null) {
                    error = ErrorMessages.SEARCH_REQ_ERROR
                }
                new_intent = Intent(context,ErrorActivity::class.java)
                new_intent.putExtra(IntentKeys.ERROR,error)
                context?.startActivity(new_intent)
            }
            ServiceResponses.LOGIN_SUCCESS -> {
                val login_info = intent.extras?.getParcelable<mLogin>(IntentKeys.LOGIN_INFO)
                val expire_date = DateUtils.addSecondsToCurrentTime(login_info!!.expires_in)

                (context?.applicationContext as App).let {
                    it.SHARED_PREFS.edit().putString(SharedPreferencesKeys.USER_NAME,login_info.user_name).apply()
                    it.SHARED_PREFS.edit().putString(SharedPreferencesKeys.AUTH_TOKEN,login_info.access_token).apply()
                    it.SHARED_PREFS.edit().putLong(SharedPreferencesKeys.EXPIRE_DATE,expire_date).apply()

                    val device_token = FirebaseInstanceId.getInstance(it.firebaseApp).token
                    val json_body = JSONObject()
                    json_body.put("device_id",device_token)
                    PostAction(
                            it,
                            it.urlBuilder.buildRegisterDeviceUrl(),
                            login_info.access_token,
                            json_body,
                            {_ ->
                                it.SHARED_PREFS.edit().putString(SharedPreferencesKeys.DEVICE_TOKEN, device_token).apply()},
                            {_ ->
                            }
                    )
                }
                new_intent = Intent(context, NetworkService::class.java)
                new_intent.putExtra(IntentKeys.ACTION,ServiceActions.GET_USER_SUBSCRIPTIONS)
                context.startService(new_intent)
            }
            ServiceResponses.LOGIN_FAILURE -> {
                var error = intent.extras?.getString(IntentKeys.ERROR)
                if (error==null) {
                    error = ErrorMessages.LOGIN_ERROR
                }

                val is_auto_login = intent.extras.getBoolean(IntentKeys.IS_AUTO_LOGIN)
                if (is_auto_login) {
                    new_intent = Intent(context,ErrorActivity::class.java)
                    new_intent.putExtra(IntentKeys.ERROR,error)
                    context?.startActivity(new_intent)
                }
                else {
                    (context?.applicationContext as App).SHARED_PREFS.let {
                        it.edit().remove(SharedPreferencesKeys.USER_EMAIL).apply()
                        it.edit().remove(SharedPreferencesKeys.USER_PASSWORD).apply()
                    }

                    activity.let {
                        (it.findViewById(R.id.text_login_error) as TextView).text = error
                        (it.findViewById(R.id.text_login_email) as EditText).text.clear()
                        (it.findViewById(R.id.text_login_password) as EditText).text.clear()
                    }
                }
            }
            ServiceResponses.REGISTRATION_SUCCESS -> {
                new_intent = Intent(context, LoginActivity::class.java)
                context?.startActivity(new_intent)
            }
            ServiceResponses.REGISTRATION_FAILURE -> {
                var error = intent.extras?.getString(IntentKeys.ERROR)
                if (error==null) {
                    error = ErrorMessages.REGISTRATION_REQ_ERROR
                }

                (context?.applicationContext as App).SHARED_PREFS.let {
                    it.edit().remove(SharedPreferencesKeys.USER_EMAIL).apply()
                    it.edit().remove(SharedPreferencesKeys.USER_PASSWORD).apply()
                    it.edit().remove(SharedPreferencesKeys.USER_NAME).apply()
                }

                activity.let {
                    (it.findViewById(R.id.text_register_error) as TextView).text = error
                    (it.findViewById(R.id.text_register_email) as EditText).text.clear()
                    (it.findViewById(R.id.text_register_password) as EditText).text.clear()
                    (it.findViewById(R.id.text_register_confirm_password) as EditText).text.clear()
                    (it.findViewById(R.id.text_register_username) as EditText).text.clear()
                }
            }
            ServiceResponses.SERVICE_REQUEST_SUCCESS -> {
                val service = intent.extras?.getParcelable<mService>(IntentKeys.SERVICE)
                new_intent = Intent(context, ServiceActivity::class.java)
                new_intent.putExtra(IntentKeys.SERVICE, service)
                context?.startActivity(new_intent)
            }
            ServiceResponses.SERVICE_REQUEST_FAILURE -> {
                var error = intent.extras?.getString(IntentKeys.ERROR)
                if (error==null) {
                    error = ErrorMessages.SERVICE_REQ_ERROR
                }
                new_intent = Intent(context,ErrorActivity::class.java)
                new_intent.putExtra(IntentKeys.ERROR,error)
                context?.startActivity(new_intent)
            }
            ServiceResponses.USER_EVENTS_REQUEST_SUCCESS -> {
                val events_info = intent.extras?.getParcelable<mUserEventWrapper>(IntentKeys.EVENTS_INFO)
                new_intent = Intent(context, EventActivity::class.java)
                new_intent.putExtra(IntentKeys.EVENTS_INFO, events_info)
                context?.startActivity(new_intent)
            }
            ServiceResponses.USER_EVENTS_REQUEST_FAILURE -> {
                var error = intent.extras?.getString(IntentKeys.ERROR)
                if (error==null) {
                    error = ErrorMessages.USER_EVENTS_REQ_ERROR
                }
                new_intent = Intent(context,ErrorActivity::class.java)
                new_intent.putExtra(IntentKeys.ERROR,error)
                context?.startActivity(new_intent)
            }
            ServiceResponses.RANKING_POST_SUCESS -> {
                val id = intent.extras.getInt(IntentKeys.SERVICE_ID)
                Toast.makeText(context,context?.getString(R.string.text_added_ranking), Toast.LENGTH_SHORT).show()
                new_intent = Intent(context, NetworkService::class.java)
                new_intent.putExtra(IntentKeys.ACTION, ServiceActions.GET_SERVICE)
                new_intent.putExtra(IntentKeys.SERVICE_ID,id)
                context?.startService(new_intent)
            }
            ServiceResponses.RANKING_POST_FAILURE -> {
                var error = intent.extras?.getString(IntentKeys.ERROR)
                if (error==null) {
                    error = ErrorMessages.RANK_POST_ERROR
                }
                new_intent = Intent(context,ErrorActivity::class.java)
                new_intent.putExtra(IntentKeys.ERROR,error)
                context?.startActivity(new_intent)
            }
            ServiceResponses.SUBSCRIPTION_ACTION_SUCCESS -> {
                val subscribing = intent.extras?.getBoolean(IntentKeys.IS_SUBSCRIBING)
                if (subscribing!!) {
                    Toast.makeText(context,context?.getString(R.string.text_added_subscription), Toast.LENGTH_SHORT).show()
                }
                else {
                    Toast.makeText(context,context?.getString(R.string.text_removed_subscription), Toast.LENGTH_SHORT).show()
                }
            }
            ServiceResponses.SUBSCRIPTION_ACTION_FAILURE -> {
                var error = intent.extras?.getString(IntentKeys.ERROR)
                if (error==null) {
                    error = ErrorMessages.SUBSCRIPTION_ACTION_ERROR
                }
                new_intent = Intent(context,ErrorActivity::class.java)
                new_intent.putExtra(IntentKeys.ERROR,error)
                context?.startActivity(new_intent)
            }
            ServiceResponses.EDIT_USERNAME_SUCCESS -> {
                Toast.makeText(context,context?.getString(R.string.text_changed_username), Toast.LENGTH_SHORT).show()
            }
            ServiceResponses.EDIT_USERNAME_FAILURE -> {
                var error = intent.extras?.getString(IntentKeys.ERROR)
                if (error==null) {
                    error = ErrorMessages.USERNAME_EDIT_ERROR
                }
                new_intent = Intent(context,ErrorActivity::class.java)
                new_intent.putExtra(IntentKeys.ERROR,error)
                context?.startActivity(new_intent)
            }
            ServiceResponses.CHANGE_PASSWORD_SUCCESS -> {
                Toast.makeText(context,context?.getString(R.string.text_changed_password), Toast.LENGTH_SHORT).show()
            }
            ServiceResponses.CHANGE_PASSWORD_FAILURE -> {
                var error = intent.extras?.getString(IntentKeys.ERROR)
                if (error==null) {
                    error = ErrorMessages.PASSWORD_EDIT_ERROR
                }
                new_intent = Intent(context,ErrorActivity::class.java)
                new_intent.putExtra(IntentKeys.ERROR,error)
                context?.startActivity(new_intent)
            }
            ServiceResponses.NO_CONNECTION -> {
                new_intent = Intent(context,ErrorActivity::class.java)
                new_intent.putExtra(IntentKeys.ERROR,ErrorMessages.NO_WIFI_CONNECTION)
                context?.startActivity(new_intent)
            }
            ServiceResponses.NOTIFICATION_RECEIVED -> {
                val title = intent.extras?.getString(IntentKeys.NOTIFICATION_TITLE)
                val body = intent.extras?.getString(IntentKeys.NOTIFICATION_BODY)
                Toast.makeText(context,"$title\n$body",Toast.LENGTH_SHORT).show()
            }
        }
    }
}