package isel.ps.ps_userclient.presentations

import android.content.Intent
import android.os.Bundle
import isel.ps.ps_userclient.App
import isel.ps.ps_userclient.R
import isel.ps.ps_userclient.models.parcelables.mServiceWrapper
import isel.ps.ps_userclient.services.NetworkService
import isel.ps.ps_userclient.utils.adapters.AdaptersUtils
import isel.ps.ps_userclient.utils.adapters.ServiceAdapter
import isel.ps.ps_userclient.utils.constants.IntentKeys
import isel.ps.ps_userclient.utils.constants.ServiceActions
import isel.ps.ps_userclient.utils.services.ServiceUtils
import kotlinx.android.synthetic.main.activity_subscription.*

class SubscriptionActivity : BaseActivity() {

    override val layoutResId: Int = R.layout.activity_subscription
    override val actionBarId: Int? = R.id.toolbar
    override val actionBarMenuResId: Int? = R.menu.main_menu

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val service_info = intent?.extras?.getParcelable<mServiceWrapper>(IntentKeys.SERVICE_INFO)
        subscriptionsView.adapter=ServiceAdapter((application as App),this, AdaptersUtils.setListServices(service_info?.services))

        text_subscription_page.text = service_info?.curr_page.toString()

        btn_subs_next.isEnabled = ServiceUtils.checkLinksHasRelField(service_info?._links,"next")
        btn_subs_next.setOnClickListener {
            val new_intent = Intent(this, NetworkService::class.java)
            new_intent.putExtra(IntentKeys.ACTION, ServiceActions.GET_USER_SUBSCRIPTIONS)
            new_intent.putExtra(IntentKeys.PAGE_REQUEST, service_info?.curr_page!! +1)
            startService(new_intent)

        }
        btn_subs_prev.isEnabled = ServiceUtils.checkLinksHasRelField(service_info?._links,"prev")
        btn_subs_prev.setOnClickListener {
            val new_intent = Intent(this, NetworkService::class.java)
            new_intent.putExtra(IntentKeys.ACTION, ServiceActions.GET_USER_SUBSCRIPTIONS)
            new_intent.putExtra(IntentKeys.PAGE_REQUEST, service_info?.curr_page!! -1)
            startService(new_intent)
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
    }
}
