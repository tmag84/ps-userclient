package isel.ps.ps_userclient.services

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.support.v4.content.LocalBroadcastManager
import com.android.volley.VolleyError
import isel.ps.ps_userclient.App
import isel.ps.ps_userclient.models.parcelables.mServiceWrapper
import isel.ps.ps_userclient.requests.GetServiceInfo
import isel.ps.ps_userclient.utils.constants.IntentKeys
import isel.ps.ps_userclient.utils.constants.ServiceActions
import isel.ps.ps_userclient.utils.constants.SharedPreferencesKeys

class NetworkService : Service() {

    override fun onBind(intent: Intent): IBinder? = null

    private fun getUserSubscriptions(startId: Int, email:String) {
        GetServiceInfo(
                (application as App),
                (application as App).urlBuilder.buildGetSubscriptionUrl(email),
                {response->handleSubscriptionResponse(startId,response)},
                {error->handleNetworkError(startId,error)})
    }

    private fun getServicesByType(startId: Int, email:String, type:Int) {
        GetServiceInfo(
                (application as App),
                (application as App).urlBuilder.buildSearchByTypeUrl(email,type),
                {response->handleServicesSearchResponse(startId,response)},
                {error->handleNetworkError(startId,error)})
    }

    private fun getServicesByPreferences(startId: Int, email:String, list:List<String>) {
        GetServiceInfo(
                (application as App),
                (application as App).urlBuilder.buildSearchByPreferencesUrl(email,list),
                {response->handleServicesSearchResponse(startId,response)},
                {error->handleNetworkError(startId,error)})
    }

    private fun broadcast(intent:Intent) {
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent)
    }

    //function that handles results when request was made by update service
    private fun handleSubscriptionResponse(startId:Int, service_info: mServiceWrapper) {
        val intent = Intent(IntentKeys.NETWORK_RECEIVER)
        intent.putExtra(IntentKeys.SERVICE_RESPONSE,ServiceActions.SUBSCRIPTIONS_REQUEST_SUCCESS)
        intent.putExtra(IntentKeys.SERVICE_INFO,service_info)
        broadcast(intent)
        stopSelf(startId)
    }

    private fun handleServicesSearchResponse(startId:Int, service_info: mServiceWrapper) {
        val intent = Intent(IntentKeys.NETWORK_RECEIVER)
        intent.putExtra(IntentKeys.SERVICE_RESPONSE,ServiceActions.SEARCH_REQUEST_SUCCESS)
        intent.putExtra(IntentKeys.SERVICE_INFO,service_info)
        broadcast(intent)
        stopSelf(startId)
    }

    private fun handleSearchError(startId:Int) {
        val intent = Intent(IntentKeys.ERROR)
        intent.putExtra(IntentKeys.SERVICE_RESPONSE,ServiceActions.SEARCH_REQUEST_FAILURE)
        intent.putExtra(IntentKeys.ERROR,"User hasn't selected any preferences.")
        broadcast(intent)
        stopSelf(startId)
    }

    private fun handleNetworkError(startId:Int, error:VolleyError) {
        val intent = Intent(IntentKeys.ERROR)
        intent.putExtra(IntentKeys.SERVICE_RESPONSE, ServiceActions.SUBSCRIPTIONS_REQUEST_FAILURE)
        intent.putExtra(IntentKeys.ERROR, error.message)
        broadcast(intent)
        stopSelf(startId)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val action = intent?.extras?.get(IntentKeys.ACTION) as String

        when(action) {
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
                (application as App).let {
                    val set = it.SHARED_PREFS.getStringSet(SharedPreferencesKeys.PREFERED_TYPES,HashSet<String>())
                    if (set.isEmpty()) {
                        handleSearchError(startId)
                    }
                    else {
                        val list = ArrayList<String>(set)
                        getServicesByPreferences(startId,email,list)
                    }
                }
            }
            else -> {
                stopSelf(startId)
                return Service.START_NOT_STICKY
            }
        }

        return Service.START_FLAG_REDELIVERY
    }
}