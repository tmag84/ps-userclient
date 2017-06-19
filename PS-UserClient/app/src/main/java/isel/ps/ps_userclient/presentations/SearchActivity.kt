package isel.ps.ps_userclient.presentations

import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.support.v4.content.LocalBroadcastManager
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import isel.ps.ps_userclient.App

import isel.ps.ps_userclient.R
import isel.ps.ps_userclient.models.parcelables.ServiceType
import isel.ps.ps_userclient.models.parcelables.mServiceWrapper
import isel.ps.ps_userclient.receivers.NetworkReceiver
import isel.ps.ps_userclient.services.NetworkService
import isel.ps.ps_userclient.utils.adapters.AdaptersUtils
import isel.ps.ps_userclient.utils.adapters.ServiceAdapter
import isel.ps.ps_userclient.utils.constants.IntentKeys
import isel.ps.ps_userclient.utils.constants.ServiceActions
import isel.ps.ps_userclient.utils.constants.SharedPreferencesKeys
import kotlinx.android.synthetic.main.activity_search.*

class SearchActivity : BaseActivity(), AdapterView.OnItemSelectedListener {

    override val layoutResId: Int = R.layout.activity_search
    override val actionBarId: Int? = R.id.toolbar
    override val actionBarMenuResId: Int? = R.menu.main_menu

    private lateinit var myReceiver : NetworkReceiver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        myReceiver = NetworkReceiver()
        LocalBroadcastManager.getInstance(this).registerReceiver(myReceiver, IntentFilter(IntentKeys.NETWORK_RECEIVER))

        if (intent.hasExtra(IntentKeys.SERVICE_INFO)) {
            val service_info = intent.extras.getParcelable<mServiceWrapper>(IntentKeys.SERVICE_INFO)
            servicesView.adapter= ServiceAdapter((application as App),this, AdaptersUtils.setListServices(service_info.services))
        }

        val arrayAdapter = ArrayAdapter(this,android.R.layout.simple_spinner_item,(application as App).list_types)
        search_spinner.adapter = arrayAdapter
        search_spinner.onItemSelectedListener = this

        btn_spinner_search.setOnClickListener {
            val service_type : ServiceType = search_spinner.getItemAtPosition(search_spinner.getSelectedItemPosition()) as ServiceType
            val intent_request = Intent(this, NetworkService::class.java)
            intent_request.putExtra(IntentKeys.ACTION, ServiceActions.SEARCH_BY_TYPE)
            intent_request.putExtra(IntentKeys.SERVICE_TYPE,service_type.id_type)
            val email = (application as App).SHARED_PREFS.getString(SharedPreferencesKeys.USER_EMAIL,"")
            intent_request.putExtra(IntentKeys.USER_EMAIL,email)
            startService(intent_request)
        }
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        LocalBroadcastManager.getInstance(this).unregisterReceiver(myReceiver)
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {}

    override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {}
}
