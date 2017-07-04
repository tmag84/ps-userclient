package isel.ps.ps_userclient.requests.base_classes

import com.android.volley.NetworkResponse
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.json.JSONObject
import java.io.IOException

class PutRequest(url: String,
                      val auth_token: String,
                      json_body: JSONObject,
                      success: (JSONObject) -> Unit,
                      error: (VolleyError) -> Unit)
    : JsonObjectRequest(Request.Method.PUT, url, json_body, success, error) {

    companion object {
        val mapper: ObjectMapper = jacksonObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
    }

    override fun parseNetworkResponse(response: NetworkResponse): Response<JSONObject> {
        try {
            val dto = PutRequest.mapper.readValue(response.data, JSONObject::class.java)
            return Response.success(dto, null)
        } catch (e: IOException) {
            e.printStackTrace()
            val error = GetRequest.mapper.readValue(response.data, Error::class.java)
            val volley_error = VolleyError(error.message)
            return Response.error(volley_error)
        }
    }

    override fun getHeaders(): MutableMap<String, String> {
        val headers = super.getHeaders()
        headers.put("Authorization","bearer $auth_token")
        return headers
    }
}