package isel.ps.ps_userclient.utils.adapters

import isel.ps.ps_userclient.models.ListServices
import isel.ps.ps_userclient.models.parcelables.mService
import java.util.*

class AdaptersUtils {
    companion object {
        fun setListServices(services: List<mService>?) : ArrayList<ListServices> {
            val array = ArrayList<ListServices>()
            if (services==null) return array
            services.sortedWith(compareBy({it.avg_rank}))
            services.forEach {
                val elem = ListServices(
                        it.name,
                        it.id,
                        it.avg_rank,
                        it.n_subscribers,
                        it.subscribed
                )
                array.add(elem)
            }
            return array
        }
    }
}
