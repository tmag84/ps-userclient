package isel.ps.ps_userclient.presentations

import android.content.Intent
import android.os.Bundle
import isel.ps.ps_userclient.R
import isel.ps.ps_userclient.utils.constants.IntentKeys
import isel.ps.ps_userclient.utils.constants.ServiceActions
import kotlinx.android.synthetic.main.activity_error.*

class ErrorActivity : BaseActivity() {

    override val layoutResId: Int = R.layout.activity_error

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val error = intent.extras?.getString(IntentKeys.ERROR)
        error_text_error.text = error

        if (error=="No Wifi Connection") {
            btn_error_back.text = getString(R.string.btn_error_back_to_login)
        }
        else {
            btn_error_back.text = getString(R.string.btn_error_back_to_subscriber)
        }

        btn_error_back.setOnClickListener {
            if (error=="No Wifi Connection") {
                startActivity(Intent(this,LoginActivity::class.java))
            }
            else {
                val request_intent = Intent(this,SubscriptionActivity::class.java)
                request_intent.putExtra(IntentKeys.ACTION, ServiceActions.GET_USER_SUBSCRIPTIONS)
                startService(request_intent)
            }
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
