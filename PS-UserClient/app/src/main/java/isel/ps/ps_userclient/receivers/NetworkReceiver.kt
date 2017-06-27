package isel.ps.ps_userclient.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import isel.ps.ps_userclient.App
import isel.ps.ps_userclient.models.parcelables.mServiceWrapper
import isel.ps.ps_userclient.presentations.SearchActivity
import isel.ps.ps_userclient.presentations.SubscriptionActivity
import isel.ps.ps_userclient.utils.constants.IntentKeys
import isel.ps.ps_userclient.utils.constants.ServiceResponses

class NetworkReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val serv_response = intent?.extras?.get(IntentKeys.SERVICE_RESPONSE)
        val new_intent : Intent

        when (serv_response) {
            ServiceResponses.SUBSCRIPTIONS_REQUEST_SUCCESS -> {
                val service_info = intent.extras?.getParcelable<mServiceWrapper>(IntentKeys.SERVICE_INFO)

                (context?.applicationContext as App).let {
                    if (it.list_types.isEmpty() || it.list_types.size!= service_info!!.list_service_types.size) {
                        it.list_types = service_info!!.list_service_types
                    }
                }

                new_intent = Intent(context, SubscriptionActivity::class.java)
                new_intent.putExtra(IntentKeys.SERVICE_INFO, service_info)
                context.startActivity(new_intent)
            }
            ServiceResponses.SUBSCRIPTIONS_REQUEST_FAILURE -> {
                val error = intent.extras?.getString(IntentKeys.ERROR)
            }
            ServiceResponses.SEARCH_REQUEST_SUCCESS -> {
                val service_info = intent.extras?.getParcelable<mServiceWrapper>(IntentKeys.SERVICE_INFO)
                new_intent = Intent(context, SearchActivity::class.java)
                new_intent.putExtra(IntentKeys.SERVICE_INFO, service_info)
                context?.startActivity(new_intent)
            }
            ServiceResponses.SEARCH_REQUEST_FAILURE -> {
                val error = intent.extras?.getString(IntentKeys.ERROR)
            }
            ServiceResponses.LOGIN_SUCCESS -> {
            }
            ServiceResponses.LOGIN_FAILURE -> {
                val error = intent.extras?.getString(IntentKeys.ERROR)
            }
            ServiceResponses.REGISTRATION_SUCCESS -> {
            }
            ServiceResponses.REGISTRATION_FAILURE -> {
                val error = intent.extras?.getString(IntentKeys.ERROR)
            }
            ServiceResponses.RANKING_POST_SUCESS -> {

            }
            ServiceResponses.RANKING_POST_FAILURE -> {
                val error = intent.extras?.getString(IntentKeys.ERROR)
            }
            ServiceResponses.SUBSCRIPTION_ACTION_SUCCESS -> {

            }
            ServiceResponses.SUBSCRIPTION_ACTION_SUCCESS -> {
                val error = intent.extras?.getString(IntentKeys.ERROR)
            }
        }
    }
}
