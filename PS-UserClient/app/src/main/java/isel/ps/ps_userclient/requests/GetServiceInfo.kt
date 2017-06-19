package isel.ps.ps_userclient.requests

import com.android.volley.VolleyError
import isel.ps.ps_userclient.App
import isel.ps.ps_userclient.models.parcelables.mServiceWrapper
import isel.ps.ps_userclient.requests.base_classes.GetRequest

class GetServiceInfo(
        app: App,
        url: String,
        success:(service_info: mServiceWrapper) -> Unit,
        failure:(error:VolleyError)-> Unit)
{
    val func_success = success
    val func_failure = failure

    init {
        val requestQueue = app.requestQueue

        //request for current weather information
        requestQueue.add(
                GetRequest(
                        url,
                        mServiceWrapper::class.java,
                        {result->handleResult(result)},
                        {error->handleError(error)}
                )
        )
    }

    //function that handles volley errors
    private fun handleError(error: VolleyError) {
        func_failure.invoke(error)
    }

    //function that handles successfull requests
    private fun handleResult(info: mServiceWrapper) {
        func_success.invoke(info)
    }
}