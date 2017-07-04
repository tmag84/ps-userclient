package isel.ps.ps_userclient.models

class ListEvents(
        val service_id:Int,
        val service_type:Int,
        val service_name:String,
        val event_id:Int,
        val text:String,
        val event_date:String
)
