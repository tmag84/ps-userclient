package isel.ps.ps_userclient.requests

import com.android.volley.VolleyError
import isel.ps.ps_userclient.App
import isel.ps.ps_userclient.models.mUserEventWrapper
import isel.ps.ps_userclient.requests.base_classes.GetRequest

class GetUserEventsInfo(
        app: App,
        url: String,
        auth_token: String,
        success:(events_info: mUserEventWrapper) -> Unit,
        failure:(error:VolleyError)-> Unit)
{
    val func_success = success
    val func_failure = failure

    init {
        val requestQueue = app.requestQueue
        requestQueue.add(
                GetRequest(
                        url,
                        mUserEventWrapper::class.java,
                        auth_token,
                        {result->func_success.invoke(result)},
                        {error->func_failure.invoke(error)}
                )
        )
    }
}