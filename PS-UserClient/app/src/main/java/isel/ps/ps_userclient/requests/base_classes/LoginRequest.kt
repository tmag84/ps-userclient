package isel.ps.ps_userclient.requests.base_classes

import com.android.volley.NetworkResponse
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonRequest
import isel.ps.ps_userclient.models.mError
import isel.ps.ps_userclient.models.mLogin
import isel.ps.ps_userclient.utils.mapper.Mapper
import org.json.JSONObject
import java.io.IOException

class LoginRequest(
        url:String,
        body: String,
        success: (mLogin) -> Unit,
        error: (VolleyError) -> Unit
) : JsonRequest<mLogin>(Request.Method.POST,url,body,success,error) {

    private val dtoType: Class<mLogin> = mLogin::class.java

    override fun parseNetworkResponse(response: NetworkResponse): Response<mLogin> {
        try {
            val dto = Mapper.mapper.readValue(response.data, dtoType)
            return Response.success(dto, null)
        } catch (e: IOException) {
            e.printStackTrace()
            val error = Mapper.mapper.readValue(response.data, mError::class.java)
            val volley_error = VolleyError(error.detail)
            return Response.error(volley_error)
        }
    }

    override fun getBodyContentType(): String {
        return "application/x-www-form-urlencoded; charset=UTF-8"
    }

    override fun parseNetworkError(volleyError: VolleyError): VolleyError {
        if (volleyError.networkResponse != null && volleyError.networkResponse.data != null) {
            try {
                val json = JSONObject(String(volleyError.networkResponse.data))
                val volley = VolleyError(json.getString("error_description"))
                return volley
            }
            catch (e: IOException) {
                val error = String(volleyError.networkResponse.data)
                e.printStackTrace()
                return VolleyError(error)
            }
        }
        return volleyError
    }
}