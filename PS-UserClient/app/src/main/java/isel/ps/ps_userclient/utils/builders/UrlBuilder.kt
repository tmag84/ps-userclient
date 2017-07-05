package isel.ps.ps_userclient.utils.builders

import android.content.res.Resources
import isel.ps.ps_userclient.R

class UrlBuilder(res: Resources) {
    private val resources : Resources = res
    private val baseUrl = resources.getString(R.string.api_url)
    private val userUrl = resources.getString(R.string.api_user)

    fun buildLoginUrl() = "$baseUrl/token"

    fun buildRegisterUrl() : String {
        val path = resources.getString(R.string.api_post_register)
        return "$baseUrl/$userUrl/$path"
    }

    fun buildGetSubscriptionUrl(page: Int?) : String {
        val path = resources.getString(R.string.api_get_subscriptions)
        if (page==null) {
            return "$baseUrl/$userUrl/$path"
        }
        val query = "page=$page"
        return "$baseUrl/$userUrl/$path?$query"
    }

    fun buildSearchByTypeUrl(type:Int, page:Int?) : String {
        val path = resources.getString(R.string.api_get_search_by_type)
        val query = "${resources.getString(R.string.query_type)}=$type"
        if (page==null) {
            return "$baseUrl/$userUrl/$path?$query"
        }
        return "$baseUrl/$userUrl/$path?$query+&page=$page"
    }

    fun buildSearchByPreferencesUrl(list:List<String>, page:Int?) : String {
        val path = resources.getString(R.string.api_get_search_by_preferences)
        var query = ""
        var i = 0
        while (i<list.size) {
            var q = "${resources.getString(R.string.query_service_types)}=${list[i]}"
            if (i<list.size-1) {
                q += "&"
            }
            query+=q
            i++
        }

        if (page==null) {
            return "$baseUrl/$userUrl/$path?$query"
        }
        return "$baseUrl/$userUrl/$path?$query+&page=$page"
    }

    fun buildGetServiceUrl(id:Int) : String {
        val path = resources.getString(R.string.api_get_service)
        val query = "${resources.getString(R.string.query_service_id)}=$id"
        return "$baseUrl/$userUrl/$path?$query"
    }

    fun buildGetUserEventsUrl(page:Int?) : String {
        val path = resources.getString(R.string.api_get_user_event)
        if (page==null) {
            return "$baseUrl/$userUrl/$path"
        }
        return "$baseUrl/$userUrl/$path?&page=$page"
    }

    fun buildSubscriptionUrl(subscribe:Boolean) : String {
        val path : String
        if (subscribe) {
            path = resources.getString(R.string.api_post_add_subscription)
        }
        else {
            path = resources.getString(R.string.api_post_remove_subscription)
        }
        return "$baseUrl/$userUrl/$path"
    }

    fun buildChangePassUrl() : String {
        val path = resources.getString(R.string.api_put_edit_password)
        return "$baseUrl/$userUrl/$path"
    }

    fun buildEditUserName() : String {
        val path = resources.getString(R.string.api_put_edit_user)
        return "$baseUrl/$userUrl/$path"
    }
 }
