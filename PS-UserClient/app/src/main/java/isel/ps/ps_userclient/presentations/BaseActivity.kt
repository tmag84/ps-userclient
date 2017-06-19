package isel.ps.ps_userclient.presentations

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import isel.ps.ps_userclient.R
import isel.ps.ps_userclient.utils.constants.IntentKeys
import isel.ps.ps_userclient.utils.constants.ServiceActions

abstract class BaseActivity : AppCompatActivity() {

    protected abstract val layoutResId: Int
    protected open val actionBarId: Int? = null
    protected open val actionBarMenuResId: Int? = null

    private fun initContents() {
        setContentView(layoutResId)
        actionBarId?.let {
            setSupportActionBar(findViewById(it) as Toolbar)
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
        actionBarMenuResId?.let {
            menuInflater.inflate(R.menu.main_menu, menu)
            return true
        }

        return super.onCreateOptionsMenu(menu)
    }


    override fun onOptionsItemSelected(item: MenuItem) : Boolean {
        when(item.itemId) {
            R.id.menu_search -> {
                startActivity(Intent(this, SearchActivity::class.java))
            }

            R.id.menu_subscriptions -> {
                startActivity(Intent(this, SubscriptionActivity::class.java))
            }

            R.id.menu_profile -> {
                startActivity(Intent(this,ProfileActivity::class.java))
            }
            else -> return super.onOptionsItemSelected(item)
        }
        return true
    }
}