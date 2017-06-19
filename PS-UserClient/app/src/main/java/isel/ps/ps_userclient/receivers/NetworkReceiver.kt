package isel.ps.ps_userclient.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import isel.ps.ps_userclient.App
import isel.ps.ps_userclient.models.parcelables.mServiceWrapper
import isel.ps.ps_userclient.presentations.SearchActivity
import isel.ps.ps_userclient.presentations.SubscriptionActivity
import isel.ps.ps_userclient.utils.constants.IntentKeys
import isel.ps.ps_userclient.utils.constants.ServiceActions

class NetworkReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val new_intent : Intent
        if (intent?.extras?.get(IntentKeys.ERROR)!=null) {
            val error = intent.extras?.getString(IntentKeys.ERROR)
            //new_intent = Intent(context, ErrorActivity::class.java)
            //new_intent.putExtra(IntentKeys.ERROR,error)
            //context?.startActivity(new_intent)
        }
        else {
            val serv_response = intent?.extras?.get(IntentKeys.SERVICE_RESPONSE)
            val service_info = intent?.extras?.getParcelable<mServiceWrapper>(IntentKeys.SERVICE_INFO)

            (context?.applicationContext as App).let {
                if (it.list_types.isEmpty() || it.list_types.size!= service_info!!.list_service_types.size) {
                    it.list_types = service_info!!.list_service_types
                }
            }

            when (serv_response) {
                ServiceActions.SUBSCRIPTIONS_REQUEST_SUCCESS -> {
                    new_intent = Intent(context, SubscriptionActivity::class.java)
                    new_intent.putExtra(IntentKeys.SERVICE_INFO, service_info)
                    context.startActivity(new_intent)
                }
                ServiceActions.SEARCH_REQUEST_SUCCESS -> {
                    new_intent = Intent(context, SearchActivity::class.java)
                    new_intent.putExtra(IntentKeys.SERVICE_INFO, service_info)
                    context.startActivity(new_intent)
                }
            }
        }
    }
}
