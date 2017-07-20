package isel.ps.ps_userclient.presentations

import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.support.v4.content.LocalBroadcastManager
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import isel.ps.ps_userclient.App
import isel.ps.ps_userclient.R
import isel.ps.ps_userclient.models.mServiceWrapper
import isel.ps.ps_userclient.receivers.NetworkReceiver
import isel.ps.ps_userclient.services.NetworkService
import isel.ps.ps_userclient.utils.adapters.AdaptersUtils
import isel.ps.ps_userclient.utils.adapters.ServiceAdapter
import isel.ps.ps_userclient.utils.constants.IntentKeys
import isel.ps.ps_userclient.utils.constants.ServiceActions
import isel.ps.ps_userclient.utils.constants.SharedPreferencesKeys
import isel.ps.ps_userclient.utils.services.ServiceUtils
import kotlinx.android.synthetic.main.activity_search.*

class SearchActivity : BaseActivity(), AdapterView.OnItemSelectedListener {

    override val layoutResId: Int = R.layout.activity_search
    override val actionBarId: Int? = R.id.toolbar
    override val actionBarMenuResId: Int? = R.menu.main_menu

    private fun getSortIntentForSearchWithPreferences(list:ArrayList<String>, page:Int, sortOrder: String) : Intent {
        val new_intent = Intent(this, NetworkService::class.java)
        new_intent.putExtra(IntentKeys.ACTION, ServiceActions.SEARCH_BY_PREFERENCES)
        new_intent.putExtra(IntentKeys.SERVICE_LIST_TYPES, list)
        new_intent.putExtra(IntentKeys.PAGE_REQUEST, page)
        new_intent.putExtra(IntentKeys.SORT_ORDER, sortOrder)
        return new_intent
    }

    private fun getSortIntentForSearchByType(type:Int, page:Int, sortOrder: String) : Intent {
        val new_intent = Intent(this, NetworkService::class.java)
        new_intent.putExtra(IntentKeys.ACTION, ServiceActions.SEARCH_BY_TYPE)
        new_intent.putExtra(IntentKeys.SERVICE_TYPE,type)
        new_intent.putExtra(IntentKeys.ACTION, ServiceActions.SEARCH_BY_TYPE)
        new_intent.putExtra(IntentKeys.PAGE_REQUEST, page)
        new_intent.putExtra(IntentKeys.SORT_ORDER, sortOrder)
        return new_intent
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        myReceiver = NetworkReceiver(this)
        LocalBroadcastManager.getInstance(this).registerReceiver(myReceiver, IntentFilter(IntentKeys.NETWORK_RECEIVER))

        val set = (application as App).SHARED_PREFS.getStringSet(SharedPreferencesKeys.PREFERED_TYPES,HashSet<String>())
        val list = ArrayList<String>(set)

        val arrayAdapter = ArrayAdapter(this,android.R.layout.simple_spinner_item,(application as App).serviceTypes.getTypesList())
        search_spinner.adapter = arrayAdapter
        search_spinner.onItemSelectedListener = this

        if (intent.hasExtra(IntentKeys.SERVICE_INFO)) {
            val intent_request = Intent(this, NetworkService::class.java)
            val service_info = intent.extras.getParcelable<mServiceWrapper>(IntentKeys.SERVICE_INFO)
            servicesView.adapter= ServiceAdapter((application as App),this, AdaptersUtils.setListServices(service_info.services))

            val searchWithPreferences = intent.extras?.getBoolean(IntentKeys.SEARCH_BY_PREFERENCE)
            val service_type = intent.extras.getInt(IntentKeys.SERVICE_TYPE)

            if (searchWithPreferences==false) {
                search_spinner.setSelection(service_type-1)
            }

            if (ServiceUtils.checkLinksHasRelField(service_info._links,"next")) {
                btn_search_next.isEnabled = true
                btn_search_next.visibility = View.VISIBLE
            }
            else {
                btn_search_next.isEnabled = false
                btn_search_next.visibility = View.GONE
            }
            btn_search_next.setOnClickListener {
                if (searchWithPreferences!!) {
                    intent_request.putExtra(IntentKeys.ACTION, ServiceActions.SEARCH_BY_PREFERENCES)
                    intent_request.putExtra(IntentKeys.SERVICE_LIST_TYPES, list)
                }
                else {
                    val selected_type : String = search_spinner.getItemAtPosition(search_spinner.selectedItemPosition) as String
                    intent_request.putExtra(IntentKeys.ACTION, ServiceActions.SEARCH_BY_TYPE)
                    intent_request.putExtra(IntentKeys.SERVICE_TYPE,(application as App).serviceTypes.getServiceTypeId(selected_type))
                }
                intent_request.putExtra(IntentKeys.PAGE_REQUEST, service_info?.curr_page!! +1)
                startService(intent_request)
            }

            if (service_info.curr_page!=1 || ServiceUtils.checkLinksHasRelField(service_info._links,"prev")) {
                btn_search_prev.isEnabled = true
                btn_search_prev.visibility = View.VISIBLE
            }
            else {
                btn_search_prev.isEnabled = false
                btn_search_prev.visibility = View.GONE
            }
            btn_search_prev.setOnClickListener {
                if (searchWithPreferences!!) {
                    intent_request.putExtra(IntentKeys.ACTION, ServiceActions.SEARCH_BY_PREFERENCES)
                    intent_request.putExtra(IntentKeys.SERVICE_LIST_TYPES, list)
                }
                else {
                    val selected_type : String = search_spinner.getItemAtPosition(search_spinner.selectedItemPosition) as String
                    intent_request.putExtra(IntentKeys.ACTION, ServiceActions.SEARCH_BY_TYPE)
                    intent_request.putExtra(IntentKeys.SERVICE_TYPE,(application as App).serviceTypes.getServiceTypeId(selected_type))
                }
                intent_request.putExtra(IntentKeys.PAGE_REQUEST, service_info?.curr_page!! -1)
                startService(intent_request)
            }

            text_search_page.text = service_info.curr_page.toString()

            btn_search_sort_subscribers.setOnClickListener {
                val new_intent : Intent
                if (searchWithPreferences!!) {
                    new_intent = getSortIntentForSearchWithPreferences(list, service_info.curr_page, getString(R.string.sort_order_by_subscriptions))
                }
                else {
                    val selected_type : String = search_spinner.getItemAtPosition(search_spinner.selectedItemPosition) as String
                    val type = (application as App).serviceTypes.getServiceTypeId(selected_type)
                    new_intent = getSortIntentForSearchByType(type,service_info.curr_page,getString(R.string.sort_order_by_subscriptions))
                }
                startService(new_intent)
            }

            btn_search_sort_ranking.setOnClickListener {
                val new_intent : Intent
                if (searchWithPreferences!!) {
                    new_intent = getSortIntentForSearchWithPreferences(list, service_info.curr_page, getString(R.string.sort_order_by_ranking))
                }
                else {
                    val selected_type : String = search_spinner.getItemAtPosition(search_spinner.selectedItemPosition) as String
                    val type = (application as App).serviceTypes.getServiceTypeId(selected_type)
                    new_intent = getSortIntentForSearchByType(type,service_info.curr_page,getString(R.string.sort_order_by_ranking))
                }
                startService(new_intent)
            }
        }
        else {
            btn_search_next.visibility = View.GONE
            btn_search_prev.visibility = View.GONE
            text_search_page.visibility = View.GONE
        }

        btn_spinner_search.setOnClickListener {
            (application as App).let {
                val type : String = search_spinner.getItemAtPosition(search_spinner.selectedItemPosition) as String
                val intent_request = Intent(this, NetworkService::class.java)
                intent_request.putExtra(IntentKeys.ACTION, ServiceActions.SEARCH_BY_TYPE)
                val id = it.serviceTypes.getServiceTypeId(type)
                intent_request.putExtra(IntentKeys.SERVICE_TYPE,id)
                startService(intent_request)
            }
        }

        btn_search_preferences.setOnClickListener {
            if (list.size==0) {
                Toast.makeText(this,getString(R.string.text_no_preferences_toast),Toast.LENGTH_SHORT).show()
            }
            else {
                val intent_request = Intent(this, NetworkService::class.java)
                intent_request.putExtra(IntentKeys.ACTION, ServiceActions.SEARCH_BY_PREFERENCES)
                intent_request.putExtra(IntentKeys.SERVICE_LIST_TYPES, list)
                startService(intent_request)
            }
        }
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
        LocalBroadcastManager.getInstance(this).unregisterReceiver(myReceiver)
    }

    override fun onDestroy() {
        super.onDestroy()
        LocalBroadcastManager.getInstance(this).unregisterReceiver(myReceiver)
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {}

    override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {}
}