package isel.ps.ps_userclient.models

class ListServices(
        val service_name:String,
        val service_id:Int,
        val avg_rank:Double,
        val n_subscribers:Int,
        val subscribed:Boolean)