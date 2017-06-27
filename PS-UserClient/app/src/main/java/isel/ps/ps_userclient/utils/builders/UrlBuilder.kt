package isel.ps.ps_userclient.utils.builders

import android.content.res.Resources
import isel.ps.ps_userclient.R

class UrlBuilder(res: Resources) {
    private val resources : Resources = res
    private val baseUrl = resources.getString(R.string.api_url)

    fun buildLoginUrl() = "$baseUrl/token"

    fun buildRegisterUrl() : String {
        val path = resources.getString(R.string.api_post_register)
        return "$baseUrl/$path"
    }

    fun buildGetServiceTypesUrl() : String {
        val path = resources.getString(R.string.api_get_service_types)
        return "$baseUrl/$path"
    }

    fun buildGetSubscriptionUrl(email:String) : String {
        val path = resources.getString(R.string.api_get_subscriptions)
        val query = "${resources.getString(R.string.query_email)}=$email"
        return "$baseUrl/$path?$query"
    }

    fun buildSearchByTypeUrl(email:String, type:Int) : String {
        val path = resources.getString(R.string.api_get_search_by_type)
        val query = "${resources.getString(R.string.query_email)}=$email&${resources.getString(R.string.query_type)}=$type"
        return "$baseUrl/$path?$query"
    }

    fun buildSearchByPreferencesUrl(email:String, list:List<String>) : String {
        val path = resources.getString(R.string.api_get_search_by_preferences)
        var query = "${resources.getString(R.string.query_email)}=$email&/${resources.getString(R.string.query_list_type)}"
        var i = 0
        while (i<list.size) {
            var q = "${resources.getString(R.string.query_list_type_id)}=${list[i]}"
            if (i<list.size-1) {
                q = q+"&"
            }
            query+=q
            i++
        }
        return "$baseUrl/$path?$query"
    }
 }
