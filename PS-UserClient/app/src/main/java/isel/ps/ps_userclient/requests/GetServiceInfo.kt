package isel.ps.ps_userclient.requests

import com.android.volley.VolleyError
import isel.ps.ps_userclient.App
import isel.ps.ps_userclient.models.mService
import isel.ps.ps_userclient.requests.base_classes.GetRequest

class GetServiceInfo(
        app: App,
        url: String,
        auth_token: String,
        success:(service_info: mService) -> Unit,
        failure:(error:VolleyError)-> Unit)
{
    val func_success = success
    val func_failure = failure

    init {
        val requestQueue = app.requestQueue
        requestQueue.add(
                GetRequest(
                        url,
                        mService::class.java,
                        auth_token,
                        {result->func_success.invoke(result)},
                        {error->func_failure.invoke(error)}
                )
        )
    }
}