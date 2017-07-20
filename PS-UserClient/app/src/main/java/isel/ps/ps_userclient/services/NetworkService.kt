package isel.ps.ps_userclient.services

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.support.v4.content.LocalBroadcastManager
import com.android.volley.VolleyError
import isel.ps.ps_userclient.App
import isel.ps.ps_userclient.models.mServiceWrapper
import isel.ps.ps_userclient.presentations.LoginActivity
import isel.ps.ps_userclient.requests.*
import isel.ps.ps_userclient.requests.base_classes.LoginRequest
import isel.ps.ps_userclient.requests.base_classes.RegisterRequest
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

    private fun loginUser(startId: Int, email: String, password: String, auto_login:Boolean) {
        val body = "grant_type=password&password=$password&userName=$email"
        (application as App).let {
            it.requestQueue.add(
                    LoginRequest(
                            it.urlBuilder.buildLoginUrl(),
                            body,
                            {result ->
                                (application as App).let {
                                    it.SHARED_PREFS.edit().putString(SharedPreferencesKeys.USER_EMAIL,email).apply()
                                    it.SHARED_PREFS.edit().putString(SharedPreferencesKeys.USER_PASSWORD,password).apply()
                                }

                                val intent_req = Intent(IntentKeys.NETWORK_RECEIVER)
                                intent_req.putExtra(IntentKeys.SERVICE_RESPONSE,ServiceResponses.LOGIN_SUCCESS)
                                intent_req.putExtra(IntentKeys.LOGIN_INFO, result)
                                intent_req.putExtra(IntentKeys.IS_AUTO_LOGIN, auto_login)
                                broadcast(intent_req)
                                stopSelf(startId)
                            },
                            {error ->
                                val intent_req = Intent(IntentKeys.NETWORK_RECEIVER)
                                intent_req.putExtra(IntentKeys.SERVICE_RESPONSE,ServiceResponses.LOGIN_FAILURE)
                                intent_req.putExtra(IntentKeys.IS_AUTO_LOGIN, auto_login)
                                intent_req.putExtra(IntentKeys.ERROR,error.message)
                                broadcast(intent_req)
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
        json_body.put("name",username)

        (application as App).requestQueue.add(
                RegisterRequest(
                        (application as App).urlBuilder.buildRegisterUrl(),
                        json_body,
                        {_ ->
                            (application as App).let{
                                it.SHARED_PREFS.edit().putString(SharedPreferencesKeys.USER_EMAIL,email).apply()
                                it.SHARED_PREFS.edit().putString(SharedPreferencesKeys.USER_PASSWORD,password).apply()
                                it.SHARED_PREFS.edit().putString(SharedPreferencesKeys.USER_NAME,username).apply()
                            }

                            val intent = Intent(IntentKeys.NETWORK_RECEIVER)
                            intent.putExtra(IntentKeys.SERVICE_RESPONSE,ServiceResponses.REGISTRATION_SUCCESS)
                            broadcast(intent)
                            stopSelf(startId)
                        },
                        {error ->
                            val intent = Intent(IntentKeys.NETWORK_RECEIVER)
                            intent.putExtra(IntentKeys.SERVICE_RESPONSE,ServiceResponses.REGISTRATION_FAILURE)
                            intent.putExtra(IntentKeys.ERROR,error.message)
                            broadcast(intent)
                            stopSelf(startId)
                        }
                )
        )
    }

    private fun getUserSubscriptions(startId: Int, page: Int?, sortOrder: String?) {
        val expire_date = getExpire_date()

        if (DateUtils.isDateAfterCurrentDay(expire_date)) {
            GetServicesInfo(
                    (application as App),
                    (application as App).urlBuilder.buildGetSubscriptionUrl(page,sortOrder),
                    getAuth_Token(),
                    {response->handleSubscriptionResponse(startId,response)},
                    {error->handleError(startId,error, ServiceResponses.SUBSCRIPTIONS_REQUEST_FAILURE)})
        }
        else {
            loginUser(startId, getUserEmail(), getUserPassword(),true)
        }
    }

    private fun getServicesByType(startId: Int,type:Int, page: Int?, sortOrder: String?) {
        val expire_date = getExpire_date()

        if (DateUtils.isDateAfterCurrentDay(expire_date)) {
            GetServicesInfo(
                    (application as App),
                    (application as App).urlBuilder.buildSearchByTypeUrl(type,page,sortOrder),
                    getAuth_Token(),
                    {response->handleServicesSearchResponse(startId,response,false,type)},
                    {error->handleError(startId,error,ServiceResponses.SEARCH_REQUEST_FAILURE)})
        }
        else {
            loginUser(startId, getUserEmail(), getUserPassword(), true)
        }
    }

    private fun getServicesByPreferences(startId: Int, list:List<Int>,page: Int?, sortOrder:String?) {
        val expire_date = getExpire_date()

        if (DateUtils.isDateAfterCurrentDay(expire_date)) {
            GetServicesInfo(
                    (application as App),
                    (application as App).urlBuilder.buildSearchByPreferencesUrl(list,page,sortOrder),
                    getAuth_Token(),
                    {response->handleServicesSearchResponse(startId,response,true,0)},
                    {error->handleError(startId,error,ServiceResponses.SEARCH_REQUEST_FAILURE)})
        }
        else {
            loginUser(startId, getUserEmail(), getUserPassword(), true)
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
            loginUser(startId, getUserEmail(), getUserPassword(), true)
        }
    }

    private fun getUserEvents(startId: Int, page:Int?, sortOrder:String?) {
        val expire_date = getExpire_date()

        if (DateUtils.isDateAfterCurrentDay(expire_date)) {
            GetUserEventsInfo(
                    (application as App),
                    (application as App).urlBuilder.buildGetUserEventsUrl(page,sortOrder),
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
            loginUser(startId, getUserEmail(), getUserPassword(), true)
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
                    {_ ->
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
            loginUser(startId, getUserEmail(), getUserPassword(), true)
        }
    }

    private fun postRanking(startId:Int, username:String, serv_id:Int, text:String, value:Double) {
        val expire_date = getExpire_date()
        if (DateUtils.isDateAfterCurrentDay(expire_date)) {
            val json_body = JSONObject()
            json_body.put("user_name",username)
            json_body.put("service_id",serv_id)
            json_body.put("text",text)
            json_body.put("value",value)

            PostAction(
                    (application as App),
                    (application as App).urlBuilder.buildPostRankingUrl(),
                    getAuth_Token(),
                    json_body,
                    {_ ->
                        val intent = Intent(IntentKeys.NETWORK_RECEIVER)
                        intent.putExtra(IntentKeys.SERVICE_RESPONSE,ServiceResponses.RANKING_POST_SUCESS)
                        intent.putExtra(IntentKeys.SERVICE_ID,serv_id)
                        broadcast(intent)
                        stopSelf(startId)
                    },
                    {error ->
                        handleError(startId,error,ServiceResponses.RANKING_POST_FAILURE)
                    }
            )
        }
        else {
            loginUser(startId, getUserEmail(), getUserPassword(), true)
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
                    {_ ->
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
            loginUser(startId, getUserEmail(), getUserPassword(), true)
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
                    {_ ->
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
            loginUser(startId, getUserEmail(), getUserPassword(), true)
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

    private fun handleServicesSearchResponse(startId:Int, service_info: mServiceWrapper, searchWithPreferences:Boolean, type:Int) {
        val intent = Intent(IntentKeys.NETWORK_RECEIVER)
        intent.putExtra(IntentKeys.SERVICE_RESPONSE,ServiceResponses.SEARCH_REQUEST_SUCCESS)
        intent.putExtra(IntentKeys.SERVICE_INFO,service_info)
        intent.putExtra(IntentKeys.SEARCH_BY_PREFERENCE,searchWithPreferences)
        intent.putExtra(IntentKeys.SERVICE_TYPE,type)
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
                        loginUser(startId,email,password,false)
                    }
                    ServiceActions.AUTO_LOGIN -> {
                        val email = it.SHARED_PREFS.getString(SharedPreferencesKeys.USER_EMAIL,"")
                        val password = it.SHARED_PREFS.getString(SharedPreferencesKeys.USER_PASSWORD,"")
                        if (email=="" || password=="") {
                            startActivity(Intent(this, LoginActivity::class.java))
                            stopSelf(startId)
                        }
                        else {
                            loginUser(startId,email,password,true)
                        }
                    }
                    ServiceActions.REGISTER -> {
                        val email = intent.extras?.get(IntentKeys.USER_EMAIL) as String
                        val password = intent.extras?.get(IntentKeys.USER_PASSWORD) as String
                        val username = intent.extras?.get(IntentKeys.USERNAME) as String
                        registerUser(startId,email,password,username)
                    }
                    ServiceActions.GET_USER_SUBSCRIPTIONS -> {
                        if (intent.extras.containsKey(IntentKeys.SERVICE_ID)) {
                            val service_id = intent.extras.getInt(IntentKeys.SERVICE_ID)
                            getService(startId,service_id)
                        }
                        else {
                            val page = intent.extras?.get(IntentKeys.PAGE_REQUEST) as Int?
                            val sortOrder = intent.extras?.get(IntentKeys.SORT_ORDER) as String?
                            getUserSubscriptions(startId, page, sortOrder)
                        }
                    }
                    ServiceActions.SEARCH_BY_TYPE -> {
                        val type = intent.extras?.get(IntentKeys.SERVICE_TYPE) as Int
                        val page = intent.extras?.get(IntentKeys.PAGE_REQUEST) as Int?
                        val sortOrder = intent.extras?.get(IntentKeys.SORT_ORDER) as String?
                        getServicesByType(startId,type,page,sortOrder)
                    }
                    ServiceActions.SEARCH_BY_PREFERENCES -> {
                        val list = intent.extras?.get(IntentKeys.SERVICE_LIST_TYPES) as List<*>
                        val types_list = ArrayList<Int>()

                        list.forEach {
                            types_list.add(
                                    (application as App).serviceTypes.getServiceTypeId(it.toString())
                            )
                        }

                        val page = intent.extras?.get(IntentKeys.PAGE_REQUEST) as Int?
                        val sortOrder = intent.extras?.get(IntentKeys.SORT_ORDER) as String?
                        getServicesByPreferences(startId,types_list,page,sortOrder)
                    }
                    ServiceActions.GET_SERVICE -> {
                        val service = intent.extras.get(IntentKeys.SERVICE_ID) as Int
                        getService(startId,service)
                    }
                    ServiceActions.GET_USER_EVENTS -> {
                        val page = intent.extras?.get(IntentKeys.PAGE_REQUEST) as Int?
                        val sortOrder = intent.extras?.get(IntentKeys.SORT_ORDER) as String?
                        getUserEvents(startId,page,sortOrder)
                    }
                    ServiceActions.SUBSCRIBE -> {
                        val id = intent.extras?.get(IntentKeys.SERVICE_ID) as Int
                        subscribeService(startId,id,true)
                    }
                    ServiceActions.UNSUBSCRIBE -> {
                        val id = intent.extras?.get(IntentKeys.SERVICE_ID) as Int
                        subscribeService(startId,id,false)
                    }
                    ServiceActions.POST_RANKING -> {
                        val username = (application as App).SHARED_PREFS.getString(SharedPreferencesKeys.USER_NAME,"")
                        val id = intent.extras.getInt(IntentKeys.SERVICE_ID)
                        val text = intent.extras.getString(IntentKeys.RANKING_TEXT)
                        val value = intent.extras.getDouble(IntentKeys.RANKING_VALUE)
                        postRanking(startId,username,id,text,value)
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