package isel.ps.ps_userclient.utils

import android.content.Context
import isel.ps.ps_userclient.presentations.ProfileActivity
import kotlinx.android.synthetic.main.activity_profile.*
import java.util.*

class ServiceTypes {
    val service_types = HashMap<String,Int>()
    val BAR = "Bar"
    val CINEMA = "Cinema"
    val TEATRO = "Teatro"
    val DANCA = "Dança"
    val GINASIO = "Ginásio"
    val RESTAURANTE = "Restaurante"

    init {
        service_types.put(BAR,1)
        service_types.put(CINEMA,2)
        service_types.put(TEATRO,3)
        service_types.put(DANCA,4)
        service_types.put(GINASIO,5)
        service_types.put(RESTAURANTE,6)
    }

    fun getServiceTypeId(type:String) : Int {
        if (service_types.containsKey(type)) {
            return service_types[type] as Int
        }
        return -1
    }

    fun getServiceTypeName(id:Int) : String {
        when(id) {
            1 -> return BAR
            2 -> return CINEMA
            3 -> return TEATRO
            4 -> return DANCA
            5 -> return GINASIO
            6 -> return RESTAURANTE
            else -> return ""
        }
    }

    fun getTypesList() : List<String> {
        return Arrays.asList(BAR,CINEMA,TEATRO,DANCA,GINASIO,RESTAURANTE)
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
            new_preferneces.add(BAR)
        }
        if(ctx.cb_cinema.isChecked) {
            new_preferneces.add(CINEMA)
        }
        if(ctx.cb_danca.isChecked) {
            new_preferneces.add(DANCA)
        }
        if(ctx.cb_ginasio.isChecked){
            new_preferneces.add(GINASIO)
        }
        if(ctx.cb_teatro.isChecked) {
            new_preferneces.add(TEATRO)
        }
        if(ctx.cb_restaurante.isChecked) {
            new_preferneces.add(RESTAURANTE)
        }
        return new_preferneces
    }
}