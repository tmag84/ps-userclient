package isel.ps.ps_userclient.requests.base_classes

import com.android.volley.NetworkResponse
import com.android.volley.Request.Method.*
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import isel.ps.ps_userclient.models.mError
import isel.ps.ps_userclient.utils.mapper.Mapper
import org.json.JSONObject
import java.io.IOException

class RegisterRequest(url:String,
                      json_body: JSONObject,
                      success: (JSONObject) -> Unit,
                      error: (VolleyError) -> Unit)

    : JsonObjectRequest(POST, url, json_body, success, error) {

    override fun parseNetworkResponse(response: NetworkResponse): Response<JSONObject> {
        try {
            if (response.statusCode==200) {
                val json = JSONObject()
                return Response.success(json, null)
            }
            val dto = Mapper.mapper.readValue(response.data, mError::class.java)
            val volley_error = VolleyError(dto.detail)
            return Response.error(volley_error)
        }
        catch (e: IOException) {
            e.printStackTrace()
            return Response.error(VolleyError())
        }
    }

    override fun parseNetworkError(volleyError: VolleyError): VolleyError {
        if (volleyError.networkResponse != null && volleyError.networkResponse.data != null) {
            try {
                val error_dto = Mapper.mapper.readValue(volleyError.networkResponse.data, mError::class.java)
                val volley = VolleyError(error_dto.detail)
                return volley
            }
            catch (e: IOException) {
                val json = JSONObject(String(volleyError.networkResponse.data))
                e.printStackTrace()
                val error_msg = json.getString("Message")
                return VolleyError(error_msg)
            }
        }
        return volleyError
    }
}