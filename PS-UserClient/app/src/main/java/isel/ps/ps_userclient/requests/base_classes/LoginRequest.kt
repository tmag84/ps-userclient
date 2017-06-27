package isel.ps.ps_userclient.requests.base_classes

import com.android.volley.NetworkResponse
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonRequest
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import isel.ps.ps_userclient.models.parcelables.mLogin
import java.io.IOException

class LoginRequest(
        url:String,
        val email: String,
        val password: String,
        val device_id: String,
        success: (mLogin) -> Unit,
        error: (VolleyError) -> Unit
) : JsonRequest<mLogin>(Method.POST,url,"",success,error) {

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
            return Response.error(VolleyError())
        }
    }

    override fun getBodyContentType(): String {
        return "application/x-www-form-urlencoded; charset=UTF-8"
    }

    override fun getParams(): MutableMap<String, String>  {
        val params = super.getParams()
        params.put("userName", email)
        params.put("password", password)
        params.put("grant_type", "password")
        params.put("device_id", device_id)
        return params
    }
}