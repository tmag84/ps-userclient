package isel.ps.ps_userclient.requests.base_classes

import com.android.volley.NetworkResponse
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.JsonRequest
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import isel.ps.ps_userclient.models.parcelables.mLogin
import org.json.JSONObject
import java.io.IOException

class LoginRequest(
        url:String,
        body: String,
        success: (mLogin) -> Unit,
        error: (VolleyError) -> Unit
) : JsonRequest<mLogin>(Request.Method.POST,url,body,success,error) {

    private val dtoType: Class<mLogin> = mLogin::class.java

    companion object {
        val mapper: ObjectMapper = jacksonObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
    }
    override fun parseNetworkResponse(response: NetworkResponse): Response<mLogin> {
        try {
            val dto = LoginRequest.mapper.readValue(response.data, dtoType)
            return Response.success(dto, null)
        } catch (e: IOException) {
            e.printStackTrace()
            val error = GetRequest.mapper.readValue(response.data, Error::class.java)
            val volley_error = VolleyError(error.message)
            return Response.error(volley_error)
        }
    }

    override fun getBodyContentType(): String {
        return "application/x-www-form-urlencoded; charset=UTF-8"
    }
}