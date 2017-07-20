package isel.ps.ps_userclient.presentations

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import isel.ps.ps_userclient.App
import isel.ps.ps_userclient.R
import isel.ps.ps_userclient.receivers.NetworkReceiver
import isel.ps.ps_userclient.services.NetworkService
import isel.ps.ps_userclient.utils.constants.IntentKeys
import isel.ps.ps_userclient.utils.constants.ServiceActions
import isel.ps.ps_userclient.utils.constants.SharedPreferencesKeys

abstract class BaseActivity : AppCompatActivity() {

    protected abstract val layoutResId: Int
    protected open val actionBarId: Int? = null
    protected open val actionBarMenuResId: Int? = null

    protected open lateinit var myReceiver : NetworkReceiver

    private fun initContents() {
        setContentView(layoutResId)
        if (actionBarId!=null) {
            actionBarId?.let {
                setSupportActionBar(findViewById(it) as Toolbar)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initContents()
    }

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        initContents()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        if (actionBarMenuResId!=null) {
            actionBarMenuResId?.let {
                menuInflater.inflate(R.menu.main_menu, menu)
                return true
            }
        }
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem) : Boolean {
        when(item.itemId) {
            R.id.menu_search -> {
                startActivity(Intent(this, SearchActivity::class.java))
            }
            R.id.menu_subscriptions -> {
                val intent_request = Intent(this, NetworkService::class.java)
                intent_request.putExtra(IntentKeys.ACTION, ServiceActions.GET_USER_SUBSCRIPTIONS)
                (application as App).let {
                    val email = it.SHARED_PREFS.getString(SharedPreferencesKeys.USER_EMAIL,"")
                    intent_request.putExtra(IntentKeys.USER_EMAIL,email)
                }
                startService(intent_request)
            }
            R.id.menu_profile -> {
                startActivity(Intent(this,ProfileActivity::class.java))
            }
            R.id.menu_events -> {
                val intent_request = Intent(this, NetworkService::class.java)
                intent_request.putExtra(IntentKeys.ACTION, ServiceActions.GET_USER_EVENTS)
                (application as App).let {
                    val email = it.SHARED_PREFS.getString(SharedPreferencesKeys.USER_EMAIL,"")
                    intent_request.putExtra(IntentKeys.USER_EMAIL,email)
                }
                startService(intent_request)
            }
            else -> return super.onOptionsItemSelected(item)
        }
        return true
    }
}