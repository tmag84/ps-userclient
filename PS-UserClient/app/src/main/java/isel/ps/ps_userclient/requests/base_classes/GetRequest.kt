package isel.ps.ps_userclient.requests.base_classes

import com.android.volley.NetworkResponse
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonRequest
import isel.ps.ps_userclient.models.mError
import isel.ps.ps_userclient.utils.mapper.Mapper
import org.json.JSONObject
import java.io.IOException

class GetRequest<DTO>(url: String,
                      private val dtoType: Class<DTO>,
                      private val auth_token: String,
                      success: (DTO) -> Unit,
                      error: (VolleyError) -> Unit)

    : JsonRequest<DTO>(Method.GET, url, "", success, error) {

    override fun parseNetworkResponse(response: NetworkResponse): Response<DTO> {
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

    override fun getHeaders(): MutableMap<String, String> {
        val headers = HashMap<String, String>()
        headers.put("Authorization","bearer $auth_token")
        return headers
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