package isel.ps.ps_userclient.utils.services

import android.content.Context
import android.net.ConnectivityManager
import isel.ps.ps_userclient.models.parcelables.Links

class ServiceUtils {
    companion object {
        //function to check connectivity
        fun checkConnectivity(ctx: Context) : Boolean {
            val cm = ctx.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetwork = cm.activeNetworkInfo

            //check if wifi is active
            if (activeNetwork.type == ConnectivityManager.TYPE_WIFI) {
                return true
            }

            //check if data connection is active
            if (activeNetwork.type == ConnectivityManager.TYPE_MOBILE) {
                return true
            }
            return false
        }

        fun checkLinksHasRelField(links:List<Links>?, rel:String) : Boolean {
            links?.forEach {
                if (it.Rel==rel) {
                    return true
                }
            }
            return false
        }
    }
}

