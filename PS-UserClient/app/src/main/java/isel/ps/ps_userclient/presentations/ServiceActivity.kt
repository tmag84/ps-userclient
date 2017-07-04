package isel.ps.ps_userclient.presentations

import android.os.Bundle
import isel.ps.ps_userclient.R
import isel.ps.ps_userclient.models.parcelables.mService
import isel.ps.ps_userclient.utils.constants.IntentKeys
import kotlinx.android.synthetic.main.activity_service.*

class ServiceActivity : BaseActivity() {

    override val layoutResId: Int = R.layout.activity_service
    override val actionBarId: Int? = R.id.toolbar
    override val actionBarMenuResId: Int? = R.menu.main_menu

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val service = intent.extras.getParcelable<mService>(IntentKeys.SERVICE)
        title_service_title.text = service.name

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
