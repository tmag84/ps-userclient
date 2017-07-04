package isel.ps.ps_userclient.requests

import com.android.volley.VolleyError
import isel.ps.ps_userclient.App
import isel.ps.ps_userclient.requests.base_classes.PutRequest
import org.json.JSONObject

class PutAction(
        app: App,
        url: String,
        auth_token: String,
        json_body: JSONObject,
        success:(json_response: JSONObject) -> Unit,
        failure:(error: VolleyError)-> Unit)
{
    val func_success = success
    val func_failure = failure

    init {
        val requestQueue = app.requestQueue
        requestQueue.add(
                PutRequest(
                        url,
                        auth_token,
                        json_body,
                        {result->func_success.invoke(result)},
                        {error->func_failure.invoke(error)}
                )
        )
    }
}
