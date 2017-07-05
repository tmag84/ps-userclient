package isel.ps.ps_userclient.services

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.provider.Settings
import android.support.v4.content.LocalBroadcastManager
import com.android.volley.VolleyError
import isel.ps.ps_userclient.App
import isel.ps.ps_userclient.models.parcelables.mServiceWrapper
import isel.ps.ps_userclient.requests.*
import isel.ps.ps_userclient.requests.base_classes.LoginRequest
import isel.ps.ps_userclient.utils.constants.IntentKeys
import isel.ps.ps_userclient.utils.constants.ServiceActions
import isel.ps.ps_userclient.utils.constants.ServiceResponses
import isel.ps.ps_userclient.utils.constants.SharedPreferencesKeys
import isel.ps.ps_userclient.utils.dates.DateUtils
import isel.ps.ps_userclient.utils.services.ServiceUtils
import org.json.JSONObject

class NetworkService : Service() {
    override fun onBind(intent: Intent): IBinder? = null
    private fun getAuth_Token() = (application as App).SHARED_PREFS.getString(SharedPreferencesKeys.AUTH_TOKEN,"")
    private fun getExpire_date() = (application as App).SHARED_PREFS.getLong(SharedPreferencesKeys.EXPIRE_DATE,0)
    private fun getUserEmail() = (application as App).SHARED_PREFS.getString(SharedPreferencesKeys.USER_EMAIL,"")
    private fun getUserPassword() = (application as App).SHARED_PREFS.getString(SharedPreferencesKeys.USER_PASSWORD,"")

    private fun loginUser(startId: Int, email: String, password: String) {
        val device_id = Settings.Secure.getString(contentResolver,Settings.Secure.ANDROID_ID)
        val body = "grant_type=password&device_id=$device_id&password=$password&userName=$email"

        (application as App).let {
            it.requestQueue.add(
                    LoginRequest(
                            it.urlBuilder.buildLoginUrl(),
                            body,
                            {result ->
                                val intent = Intent(IntentKeys.NETWORK_RECEIVER)
                                intent.putExtra(IntentKeys.SERVICE_RESPONSE,ServiceResponses.LOGIN_SUCCESS)
                                intent.putExtra(IntentKeys.LOGIN_INFO, result)
                                broadcast(intent)
                                stopSelf(startId)
                            },
                            {error ->
                                val intent = Intent(IntentKeys.NETWORK_RECEIVER)
                                intent.putExtra(IntentKeys.SERVICE_RESPONSE,ServiceResponses.LOGIN_FAILURE)
                                intent.putExtra(IntentKeys.ERROR,error)
                                broadcast(intent)
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
                    broadcast(intent)
                    stopSelf(startId)
                },
                {error ->
                    val intent = Intent(IntentKeys.NETWORK_RECEIVER)
                    intent.putExtra(IntentKeys.SERVICE_RESPONSE,ServiceResponses.REGISTRATION_FAILURE)
                    intent.putExtra(IntentKeys.ERROR,error)
                    broadcast(intent)
                    stopSelf(startId)
                }
        )
    }

    private fun getUserSubscriptions(startId: Int, page: Int?) {
        val expire_date = getExpire_date()

        if (DateUtils.isDateAfterCurrentDay(expire_date)) {
            GetServicesInfo(
                    (application as App),
                    (application as App).urlBuilder.buildGetSubscriptionUrl(page),
                    getAuth_Token(),
                    {response->handleSubscriptionResponse(startId,response)},
                    {error->handleError(startId,error, ServiceResponses.SUBSCRIPTIONS_REQUEST_FAILURE)})
        }
        else {
            loginUser(startId, getUserEmail(), getUserPassword())
        }
    }

    private fun getServicesByType(startId: Int,type:Int, page: Int?) {
        val expire_date = getExpire_date()

        if (DateUtils.isDateAfterCurrentDay(expire_date)) {
            GetServicesInfo(
                    (application as App),
                    (application as App).urlBuilder.buildSearchByTypeUrl(type,page),
                    getAuth_Token(),
                    {response->handleServicesSearchResponse(startId,response,false)},
                    {error->handleError(startId,error,ServiceResponses.SEARCH_REQUEST_FAILURE)})
        }
        else {
            loginUser(startId, getUserEmail(), getUserPassword())
        }
    }

    private fun getServicesByPreferences(startId: Int, list:List<String>,page: Int?) {
        val expire_date = getExpire_date()

        if (DateUtils.isDateAfterCurrentDay(expire_date)) {
            GetServicesInfo(
                    (application as App),
                    (application as App).urlBuilder.buildSearchByPreferencesUrl(list,page),
                    getAuth_Token(),
                    {response->handleServicesSearchResponse(startId,response,true)},
                    {error->handleError(startId,error,ServiceResponses.SEARCH_REQUEST_FAILURE)})
        }
        else {
            loginUser(startId, getUserEmail(), getUserPassword())
        }
    }

    private fun getService(startId: Int, id:Int) {
        val expire_date = getExpire_date()

        if (DateUtils.isDateAfterCurrentDay(expire_date)) {
            GetServiceInfo(
                    (application as App),
                    (application as App).urlBuilder.buildGetServiceUrl(id),
                    getAuth_Token(),
                    {response->
                        val intent = Intent(IntentKeys.NETWORK_RECEIVER)
                        intent.putExtra(IntentKeys.SERVICE_RESPONSE,ServiceResponses.SERVICE_REQUEST_SUCCESS)
                        intent.putExtra(IntentKeys.SERVICE, response)
                        broadcast(intent)
                        stopSelf(startId)
                    },
                    {error->handleError(startId,error,ServiceResponses.SERVICE_REQUEST_FAILURE)})
        }
        else {
            loginUser(startId, getUserEmail(), getUserPassword())
        }
    }

    private fun getUserEvents(startId: Int, page:Int?) {
        val expire_date = getExpire_date()

        if (DateUtils.isDateAfterCurrentDay(expire_date)) {
            GetUserEventsInfo(
                    (application as App),
                    (application as App).urlBuilder.buildGetUserEventsUrl(page),
                    getAuth_Token(),
                    {response->
                        val intent = Intent(IntentKeys.NETWORK_RECEIVER)
                        intent.putExtra(IntentKeys.SERVICE_RESPONSE,ServiceResponses.USER_EVENTS_REQUEST_SUCCESS)
                        intent.putExtra(IntentKeys.EVENTS_INFO, response)
                        broadcast(intent)
                        stopSelf(startId)
                    },
                    {error->handleError(startId,error,ServiceResponses.USER_EVENTS_REQUEST_FAILURE)})
        }
        else {
            loginUser(startId, getUserEmail(), getUserPassword())
        }
    }

    private fun subscribeService(startId: Int, id:Int, subscribe:Boolean) {
        val expire_date = getExpire_date()
        if (DateUtils.isDateAfterCurrentDay(expire_date)) {
            val json_body = JSONObject()
            json_body.put("id",id)

            PostAction(
                    (application as App),
                    (application as App).urlBuilder.buildSubscriptionUrl(subscribe),
                    getAuth_Token(),
                    json_body,
                    {
                        val intent = Intent(IntentKeys.NETWORK_RECEIVER)
                        intent.putExtra(IntentKeys.SERVICE_RESPONSE,ServiceResponses.SUBSCRIPTION_ACTION_SUCCESS)
                        intent.putExtra(IntentKeys.IS_SUBSCRIBING,subscribe)
                        broadcast(intent)
                        stopSelf(startId)
                    },
                    {error ->
                        handleError(startId,error,ServiceResponses.SUBSCRIPTION_ACTION_FAILURE)
                    }
            )
        }
        else {
            loginUser(startId, getUserEmail(), getUserPassword())
        }
    }

    private fun changePassword(startId:Int, new_password:String) {
        val expire_date = getExpire_date()
        if (DateUtils.isDateAfterCurrentDay(expire_date)) {
            val json_body = JSONObject()
            json_body.put("new_password",new_password)

            PutAction(
                    (application as App),
                    (application as App).urlBuilder.buildChangePassUrl(),
                    getAuth_Token(),
                    json_body,
                    {
                        val intent = Intent(IntentKeys.NETWORK_RECEIVER)
                        intent.putExtra(IntentKeys.SERVICE_RESPONSE,ServiceResponses.CHANGE_PASSWORD_SUCCESS)
                        broadcast(intent)
                        stopSelf(startId)
                    },
                    {error ->
                        handleError(startId,error,ServiceResponses.CHANGE_PASSWORD_FAILURE)
                    }
            )
        }
        else {
            loginUser(startId, getUserEmail(), getUserPassword())
        }
    }

    private fun changeUserName(startId:Int, new_name:String) {
        val expire_date = getExpire_date()
        if (DateUtils.isDateAfterCurrentDay(expire_date)) {
            val json_body = JSONObject()
            json_body.put("name",new_name)

            PutAction(
                    (application as App),
                    (application as App).urlBuilder.buildEditUserName(),
                    getAuth_Token(),
                    json_body,
                    {
                        val intent = Intent(IntentKeys.NETWORK_RECEIVER)
                        intent.putExtra(IntentKeys.SERVICE_RESPONSE,ServiceResponses.EDIT_USERNAME_SUCCESS)
                        broadcast(intent)
                        stopSelf(startId)
                    },
                    {error ->
                        handleError(startId,error,ServiceResponses.EDIT_USERNAME_FAILURE)
                    }
            )
        }
        else {
            loginUser(startId, getUserEmail(), getUserPassword())
        }
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

    private fun handleServicesSearchResponse(startId:Int, service_info: mServiceWrapper, searchWithPreferences:Boolean) {
        val intent = Intent(IntentKeys.NETWORK_RECEIVER)
        intent.putExtra(IntentKeys.SERVICE_RESPONSE,ServiceResponses.SEARCH_REQUEST_SUCCESS)
        intent.putExtra(IntentKeys.SERVICE_INFO,service_info)
        intent.putExtra(IntentKeys.SEARCH_BY_PREFERENCE,searchWithPreferences)
        broadcast(intent)
        stopSelf(startId)
    }

    private fun handleError(startId:Int, error:VolleyError, serv_response:String) {
        val intent = Intent(IntentKeys.NETWORK_RECEIVER)
        intent.putExtra(IntentKeys.SERVICE_RESPONSE, serv_response)
        intent.putExtra(IntentKeys.ERROR, error.message)
        broadcast(intent)
        stopSelf(startId)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val action = intent?.extras?.get(IntentKeys.ACTION) as String

        if (!ServiceUtils.checkConnectivity(this)) {
            val new_intent = Intent(IntentKeys.NETWORK_RECEIVER)
            new_intent.putExtra(IntentKeys.SERVICE_RESPONSE,ServiceResponses.NO_CONNECTION)
            startService(new_intent)
        }
        else {
            (application as App).let {
                when(action) {
                    ServiceActions.LOGIN -> {
                        val email = intent.extras?.get(IntentKeys.USER_EMAIL) as String
                        val password = intent.extras?.get(IntentKeys.USER_PASSWORD) as String
                        loginUser(startId,email,password)
                    }
                    ServiceActions.AUTO_LOGIN -> {
                        val email = it.SHARED_PREFS.getString(SharedPreferencesKeys.USER_EMAIL,"")
                        val password = it.SHARED_PREFS.getString(SharedPreferencesKeys.USER_PASSWORD,"")
                        loginUser(startId,email,password)
                    }
                    ServiceActions.REGISTER -> {
                        val email = intent.extras?.get(IntentKeys.USER_EMAIL) as String
                        val password = intent.extras?.get(IntentKeys.USER_PASSWORD) as String
                        val username = intent.extras?.get(IntentKeys.USERNAME) as String
                        registerUser(startId,email,password,username)
                    }
                    ServiceActions.GET_USER_SUBSCRIPTIONS -> {
                        val page = intent.extras?.get(IntentKeys.PAGE_REQUEST) as Int?
                        getUserSubscriptions(startId, page)
                    }
                    ServiceActions.SEARCH_BY_TYPE -> {
                        val type = intent.extras?.get(IntentKeys.SERVICE_TYPE) as Int
                        val page = intent.extras?.get(IntentKeys.PAGE_REQUEST) as Int?
                        getServicesByType(startId,type,page)
                    }
                    ServiceActions.SEARCH_BY_PREFERENCES -> {
                        val list = intent.extras?.get(IntentKeys.SERVICE_LIST_TYPES) as List<String>
                        val page = intent.extras?.get(IntentKeys.PAGE_REQUEST) as Int?
                        getServicesByPreferences(startId,list,page)
                    }
                    ServiceActions.GET_SERVICE -> {
                        val service = intent.extras.get(IntentKeys.SERVICE_ID) as Int
                        getService(startId,service)
                    }
                    ServiceActions.GET_USER_EVENTS -> {
                        val page = intent.extras?.get(IntentKeys.PAGE_REQUEST) as Int?
                        getUserEvents(startId,page)
                    }
                    ServiceActions.SUBSCRIBE -> {
                        val id = intent.extras?.get(IntentKeys.SERVICE_ID) as Int
                        subscribeService(startId,id,true)
                    }
                    ServiceActions.UNSUBSCRIBE -> {
                        val id = intent.extras?.get(IntentKeys.SERVICE_ID) as Int
                        subscribeService(startId,id,false)
                    }
                    ServiceActions.PASSWORD_CHANGE -> {
                        val new_password = intent.extras?.get(IntentKeys.USER_PASSWORD) as String
                        changePassword(startId,new_password)
                    }
                    ServiceActions.USER_INFO_CHANGE -> {
                        val new_name = intent.extras?.get(IntentKeys.USERNAME) as String
                        changeUserName(startId, new_name)
                    }
                    else -> {
                        stopSelf(startId)
                        return Service.START_NOT_STICKY
                    }
                }
            }
        }
        return Service.START_FLAG_REDELIVERY
    }
}