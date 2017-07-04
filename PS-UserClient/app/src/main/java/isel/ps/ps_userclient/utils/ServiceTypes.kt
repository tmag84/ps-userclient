package isel.ps.ps_userclient.utils

import android.content.Context
import isel.ps.ps_userclient.presentations.ProfileActivity
import kotlinx.android.synthetic.main.activity_profile.*

class ServiceTypes() {
    val service_types = HashMap<String,Int>()

    init {
        service_types.put("Bar",1)
        service_types.put("Cinema",2)
        service_types.put("Teatro",3)
        service_types.put("Dança",4)
        service_types.put("Ginásio",5)
        service_types.put("Restaurante",6)
    }

    fun getServiceTypeId(type:String) : Int {
        return service_types.get(type)!!
    }

    fun getTypesList() : List<String> {
        return service_types.keys.toList()
    }

    fun checkBoxes(ctx:Context,list:List<String>) {
        list.forEach { checkBox(it,(ctx as ProfileActivity)) }
    }

    private fun checkBox(type:String, ctx:ProfileActivity) {
        when(getServiceTypeId(type)) {
            1 -> {
                ctx.cb_bar.isChecked = true
            }
            2 -> {
                ctx.cb_cinema.isChecked = true
            }
            3 -> {
                ctx.cb_danca.isChecked = true
            }
            4 -> {
                ctx.cb_ginasio.isChecked = true
            }
            5 -> {
                ctx.cb_teatro.isChecked = true
            }
            6 -> {
                ctx.cb_restaurante.isChecked = true
            }
            else -> {

            }
        }
    }

    fun getPreferencesList(ctx:ProfileActivity) : List<String> {
        val new_preferneces = ArrayList<String>()

        if(ctx.cb_bar.isChecked) {
            new_preferneces.add(getServiceTypeId("Bar").toString())
        }
        if(ctx.cb_cinema.isChecked) {
            new_preferneces.add(getServiceTypeId("Cinema").toString())
        }
        if(ctx.cb_danca.isChecked) {
            new_preferneces.add(getServiceTypeId("Dança").toString())
        }
        if(ctx.cb_ginasio.isChecked){
            new_preferneces.add(getServiceTypeId("Ginásio").toString())
        }
        if(ctx.cb_teatro.isChecked) {
            new_preferneces.add(getServiceTypeId("Teatro").toString())
        }
        if(ctx.cb_restaurante.isChecked) {
            new_preferneces.add(getServiceTypeId("Restaurante").toString())
        }
        return new_preferneces
    }
}