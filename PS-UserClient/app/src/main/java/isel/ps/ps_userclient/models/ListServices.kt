package isel.ps.ps_userclient.models

class ListServices(
        val service_name:String,
        val service_id:Int,
        val service_type:Int,
        val avg_rank:Double,
        var n_subscribers:Int,
        var subscribed:Boolean
)