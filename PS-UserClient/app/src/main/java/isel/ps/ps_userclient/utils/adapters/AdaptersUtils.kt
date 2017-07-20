package isel.ps.ps_userclient.utils.adapters

import isel.ps.ps_userclient.models.ListEvents
import isel.ps.ps_userclient.models.ListServices
import isel.ps.ps_userclient.models.Event
import isel.ps.ps_userclient.models.ServiceEvent
import isel.ps.ps_userclient.models.mService
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
                        it.service_type,
                        it.avg_rank,
                        it.n_subscribers,
                        it.subscribed
                )
                array.add(elem)
            }
            return array
        }

        fun setUserEvents(events:List<ServiceEvent>) : ArrayList<ListEvents> {
            val array = ArrayList<ListEvents>()
            if (events.isEmpty()) return array
            events.sortedWith(compareBy({it.event_begin}))
            events.forEach {
                val elem = ListEvents(
                        it.service_id,
                        it.service_type,
                        it.service_name,
                        it.service_location,
                        it.id,
                        it.text,
                        it.event_begin,
                        it.event_end
                )
                array.add(elem)
            }
            return array
        }

        fun setListEvents(service: mService, events:List<Event>) : ArrayList<ListEvents> {
            val list = ArrayList<ServiceEvent>()
            events.forEach {
                val ev = ServiceEvent(
                        service.id,
                        service.service_type,
                        service.name,
                        service.contact_location,
                        it.id,
                        it.text,
                        it.creation_date,
                        it.event_begin,
                        it.event_end
                        )
                list.add(ev)
            }
            return setUserEvents(list)
        }
    }
}