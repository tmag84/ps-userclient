package isel.ps.ps_userclient.services

import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.iid.FirebaseInstanceIdService
import isel.ps.ps_userclient.App
import isel.ps.ps_userclient.requests.PostAction
import isel.ps.ps_userclient.utils.constants.SharedPreferencesKeys
import org.json.JSONObject

class FcmIdService : FirebaseInstanceIdService() {

    private fun saveTokenToServer(device_id:String?) {
        (application as App).let {
            if (device_id!=null && device_id!=it.SHARED_PREFS.getString(SharedPreferencesKeys.DEVICE_TOKEN,"") &&
                    it.SHARED_PREFS.contains(SharedPreferencesKeys.AUTH_TOKEN)) {
                val device_token = FirebaseInstanceId.getInstance(it.firebaseApp).token
                val json_body = JSONObject()
                json_body.put("device_id",device_token)

                PostAction(
                        it,
                        it.urlBuilder.buildRegisterDeviceUrl(),
                        it.SHARED_PREFS.getString(SharedPreferencesKeys.AUTH_TOKEN,""),
                        json_body,
                        {_ ->
                            it.SHARED_PREFS.edit().putString(SharedPreferencesKeys.DEVICE_TOKEN, device_token).apply()},
                        {_ ->
                        }
                )
            }
        }
    }

    override fun onTokenRefresh() {
        val device_token = FirebaseInstanceId.getInstance((application as App).firebaseApp).token
        saveTokenToServer(device_token)
    }
}