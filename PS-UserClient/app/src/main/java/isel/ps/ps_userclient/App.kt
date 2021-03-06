package isel.ps.ps_userclient

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley
import com.google.firebase.FirebaseApp
import isel.ps.ps_userclient.utils.ServiceTypes
import isel.ps.ps_userclient.utils.builders.UrlBuilder
import isel.ps.ps_userclient.utils.constants.SharedPreferencesKeys

class App : Application() {
    lateinit var SHARED_PREFS : SharedPreferences
    lateinit var requestQueue : RequestQueue
    lateinit var urlBuilder : UrlBuilder
    lateinit var serviceTypes : ServiceTypes
    lateinit var firebaseApp : FirebaseApp

    fun firstTimeInit() {
        val editor = SHARED_PREFS.edit()
        editor.putStringSet(SharedPreferencesKeys.PREFERED_TYPES,HashSet<String>())
        editor.putInt("initialized",1)
        editor.apply()
    }

    override fun onCreate() {
        super.onCreate()
        SHARED_PREFS = getSharedPreferences(SharedPreferencesKeys.SHARED_PREFS_ID, Context.MODE_PRIVATE)

        //first time init to setup shared preferences
        if (!SHARED_PREFS.contains("initialized")) {
            firstTimeInit()
        }

        requestQueue = Volley.newRequestQueue(this)
        urlBuilder = UrlBuilder(resources)
        serviceTypes = ServiceTypes()
        firebaseApp = FirebaseApp.initializeApp(this) as FirebaseApp
    }
}