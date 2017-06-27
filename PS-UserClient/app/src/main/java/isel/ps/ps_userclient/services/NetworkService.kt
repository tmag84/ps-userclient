package isel.ps.ps_userclient.services

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.provider.Settings
import android.support.v4.content.LocalBroadcastManager
import com.android.volley.VolleyError
import isel.ps.ps_userclient.App
import isel.ps.ps_userclient.models.parcelables.mServiceWrapper
import isel.ps.ps_userclient.requests.GetServiceInfo
import isel.ps.ps_userclient.requests.PostAction
import isel.ps.ps_userclient.requests.base_classes.LoginRequest
import isel.ps.ps_userclient.utils.constants.IntentKeys
import isel.ps.ps_userclient.utils.constants.ServiceActions
import isel.ps.ps_userclient.utils.constants.ServiceResponses
import isel.ps.ps_userclient.utils.constants.SharedPreferencesKeys
import org.json.JSONObject

class NetworkService : Service() {

    override fun onBind(intent: Intent): IBinder? = null

    private fun loginUser(startId: Int, email: String, password: String) {
        val device_id = Settings.Secure.getString(contentResolver,Settings.Secure.ANDROID_ID)
        (application as App).let {
            it.requestQueue.add(
                    LoginRequest(
                            it.urlBuilder.buildLoginUrl(),
                            email,
                            password,
                            device_id,
                            {result ->
                                val intent = Intent(IntentKeys.NETWORK_RECEIVER)
                                intent.putExtra(IntentKeys.SERVICE_RESPONSE,ServiceResponses.LOGIN_SUCCESS)
                                intent.putExtra(IntentKeys.LOGIN_INFO, result)
                                startService(intent)
                                stopSelf(startId)
                            },
                            {error ->
                                val intent = Intent(IntentKeys.NETWORK_RECEIVER)
                                intent.putExtra(IntentKeys.SERVICE_RESPONSE,ServiceResponses.LOGIN_FAILURE)
                                intent.putExtra(IntentKeys.ERROR,error)
                                startService(intent)
                                stopSelf(startId)
                            }
                    )
            )
        }
    }

    private fun registerUser(startId: Int, email: String, password: String, username: String) {
        val json_body = JSONObject()
        json_body.put("email",email)
        json_body.put("password",password)
        json_body.put("username",username)

        PostAction(
                (application as App),
                (application as App).urlBuilder.buildRegisterUrl(),
                "auth_token",
                json_body,
                {
                    val intent = Intent(IntentKeys.NETWORK_RECEIVER)
                    intent.putExtra(IntentKeys.SERVICE_RESPONSE,ServiceResponses.REGISTRATION_SUCCESS)
                    startService(intent)
                    stopSelf(startId)
                },
                {error ->
                    val intent = Intent(IntentKeys.NETWORK_RECEIVER)
                    intent.putExtra(IntentKeys.SERVICE_RESPONSE,ServiceResponses.REGISTRATION_FAILURE)
                    intent.putExtra(IntentKeys.ERROR,error)
                    startService(intent)
                    stopSelf(startId)
                }
        )

    }

    private fun getUserSubscriptions(startId: Int, email:String) {
        GetServiceInfo(
                (application as App),
                (application as App).urlBuilder.buildGetSubscriptionUrl(email),
                "auth_token",
                {response->handleSubscriptionResponse(startId,response)},
                {error->handleNetworkError(startId,error)})
    }

    private fun getServicesByType(startId: Int, email:String, type:Int) {
        GetServiceInfo(
                (application as App),
                (application as App).urlBuilder.buildSearchByTypeUrl(email,type),
                "auth_token",
                {response->handleServicesSearchResponse(startId,response)},
                {error->handleNetworkError(startId,error)})
    }

    private fun getServicesByPreferences(startId: Int, email:String, list:List<String>) {
        GetServiceInfo(
                (application as App),
                (application as App).urlBuilder.buildSearchByPreferencesUrl(email,list),
                "auth_token",
                {response->handleServicesSearchResponse(startId,response)},
                {error->handleNetworkError(startId,error)})
    }

    private fun broadcast(intent:Intent) {
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent)
    }

    private fun handleSubscriptionResponse(startId:Int, service_info: mServiceWrapper) {
        val intent = Intent(IntentKeys.NETWORK_RECEIVER)
        intent.putExtra(IntentKeys.SERVICE_RESPONSE,ServiceResponses.SUBSCRIPTIONS_REQUEST_SUCCESS)
        intent.putExtra(IntentKeys.SERVICE_INFO,service_info)
        broadcast(intent)
        stopSelf(startId)
    }

    private fun handleServicesSearchResponse(startId:Int, service_info: mServiceWrapper) {
        val intent = Intent(IntentKeys.NETWORK_RECEIVER)
        intent.putExtra(IntentKeys.SERVICE_RESPONSE,ServiceResponses.SEARCH_REQUEST_SUCCESS)
        intent.putExtra(IntentKeys.SERVICE_INFO,service_info)
        broadcast(intent)
        stopSelf(startId)
    }

    private fun handleSearchError(startId:Int) {
        val intent = Intent(IntentKeys.ERROR)
        intent.putExtra(IntentKeys.SERVICE_RESPONSE,ServiceResponses.SEARCH_REQUEST_FAILURE)
        intent.putExtra(IntentKeys.ERROR,"User hasn't selected any preferences.")
        broadcast(intent)
        stopSelf(startId)
    }

    private fun handleNetworkError(startId:Int, error:VolleyError) {
        val intent = Intent(IntentKeys.ERROR)
        intent.putExtra(IntentKeys.SERVICE_RESPONSE, ServiceResponses.SUBSCRIPTIONS_REQUEST_FAILURE)
        intent.putExtra(IntentKeys.ERROR, error.message)
        broadcast(intent)
        stopSelf(startId)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val action = intent?.extras?.get(IntentKeys.ACTION) as String

        (application as App).let {
            when(action) {
                ServiceActions.LOGIN -> {
                    val email = it.SHARED_PREFS.getString(SharedPreferencesKeys.USER_EMAIL,"")
                    val password = it.SHARED_PREFS.getString(SharedPreferencesKeys.USER_PASSWORD,"")
                    loginUser(startId,email,password)
                }
                ServiceActions.AUTO_LOGIN -> {
                    val email = intent.extras?.get(IntentKeys.USER_EMAIL) as String
                    val password = intent.extras?.get(IntentKeys.USER_PASSWORD) as String
                    loginUser(startId,email,password)
                }
                ServiceActions.REGISTER -> {
                    val email = intent.extras?.get(IntentKeys.USER_EMAIL) as String
                    val password = intent.extras?.get(IntentKeys.USER_PASSWORD) as String
                    val username = intent.extras?.get(IntentKeys.USERNAME) as String
                    registerUser(startId,email,password,username)
                }
                ServiceActions.GET_USER_SUBSCRIPTIONS -> {
                    val email = intent.extras?.get(IntentKeys.USER_EMAIL) as String
                    getUserSubscriptions(startId,email)
                }
                ServiceActions.SEARCH_BY_TYPE -> {
                    val email = intent.extras?.get(IntentKeys.USER_EMAIL) as String
                    val type = intent.extras?.get(IntentKeys.SERVICE_TYPE) as Int
                    getServicesByType(startId,email,type)
                }
                ServiceActions.SEARCH_BY_PREFERENCES -> {
                    val email = intent.extras?.get(IntentKeys.USER_EMAIL) as String

                    val set = it.SHARED_PREFS.getStringSet(SharedPreferencesKeys.PREFERED_TYPES,HashSet<String>())
                    if (set.isEmpty()) {
                        handleSearchError(startId)
                    }
                    else {
                        val list = ArrayList<String>(set)
                        getServicesByPreferences(startId,email,list)
                    }
                }
                ServiceActions.SUBSCRIBE -> {

                }
                ServiceActions.UNSUBSCRIBE -> {

                }
                ServiceActions.PASSWORD_CHANGE -> {

                }
                ServiceActions.USER_INFO_CHANGE -> {

                }

                else -> {
                    stopSelf(startId)
                    return Service.START_NOT_STICKY
                }
            }
        }
        return Service.START_FLAG_REDELIVERY
    }
}