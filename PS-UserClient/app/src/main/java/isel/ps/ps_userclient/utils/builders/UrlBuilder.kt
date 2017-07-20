package isel.ps.ps_userclient.utils.builders

import android.content.res.Resources
import android.net.Uri
import isel.ps.ps_userclient.R

class UrlBuilder(res: Resources) {
    private val resources: Resources = res

    fun buildLoginUrl(): String {
        val builder = Uri.Builder()
        builder
                .scheme("https")
                .authority(resources.getString(R.string.api_authoritiy))
                .appendPath("token")

        return builder.toString()
    }

    fun buildRegisterUrl(): String {
        val builder = Uri.Builder()
        builder
                .scheme("https")
                .authority(resources.getString(R.string.api_authoritiy))
                .appendPath(resources.getString(R.string.api_path))
                .appendPath(resources.getString(R.string.api_user_path))
                .appendPath(resources.getString(R.string.api_post_register))
        return builder.toString()
    }

    fun buildGetSubscriptionUrl(page: Int?, sortOrder: String?): String {
        val builder = Uri.Builder()
        builder
                .scheme("https")
                .authority(resources.getString(R.string.api_authoritiy))
                .appendPath(resources.getString(R.string.api_path))
                .appendPath(resources.getString(R.string.api_user_path))
                .appendPath(resources.getString(R.string.api_get_subscriptions))

        if (page != null) {
            builder.appendQueryParameter("page", page.toString())
        }
        if (sortOrder != null) {
            builder.appendQueryParameter("sortOrder", sortOrder)
        }
        return builder.toString()
    }

    fun buildSearchByTypeUrl(type: Int, page: Int?, sortOrder: String?): String {
        val builder = Uri.Builder()
        builder
                .scheme("https")
                .authority(resources.getString(R.string.api_authoritiy))
                .appendPath(resources.getString(R.string.api_path))
                .appendPath(resources.getString(R.string.api_user_path))
                .appendPath(resources.getString(R.string.api_get_search_by_type))
                .appendQueryParameter("type", type.toString())

        if (page != null) {
            builder.appendQueryParameter("page", page.toString())
        }
        if (sortOrder != null) {
            builder.appendQueryParameter("sortOrder", sortOrder)
        }
        return builder.toString()
    }

    fun buildSearchByPreferencesUrl(list: List<Int>, page: Int?, sortOrder:String?): String {
        val builder = Uri.Builder()
        builder
                .scheme("https")
                .authority(resources.getString(R.string.api_authoritiy))
                .appendPath(resources.getString(R.string.api_path))
                .appendPath(resources.getString(R.string.api_user_path))
                .appendPath(resources.getString(R.string.api_get_search_by_preferences))

        list.forEach {
            builder.appendQueryParameter(resources.getString(R.string.query_service_types), it.toString())
        }
        if (page != null) {
            builder.appendQueryParameter("page", page.toString())
        }
        if (sortOrder != null) {
            builder.appendQueryParameter("sortOrder", sortOrder)
        }
        return builder.toString()
    }

    fun buildGetServiceUrl(id: Int): String {
        val builder = Uri.Builder()
        builder
                .scheme("https")
                .authority(resources.getString(R.string.api_authoritiy))
                .appendPath(resources.getString(R.string.api_path))
                .appendPath(resources.getString(R.string.api_user_path))
                .appendPath(resources.getString(R.string.api_get_service))
                .appendQueryParameter(resources.getString(R.string.query_service_id),id.toString())

        return builder.toString()
    }

    fun buildGetUserEventsUrl(page: Int?, sortOrder: String?): String {
        val builder = Uri.Builder()
        builder
                .scheme("https")
                .authority(resources.getString(R.string.api_authoritiy))
                .appendPath(resources.getString(R.string.api_path))
                .appendPath(resources.getString(R.string.api_user_path))
                .appendPath(resources.getString(R.string.api_get_user_event))

        if (page != null) {
            builder.appendQueryParameter("page", page.toString())
        }
        if (sortOrder != null) {
            builder.appendQueryParameter("sortOrder", sortOrder)
        }
        return builder.toString()
    }

    fun buildSubscriptionUrl(subscribe: Boolean): String {
        val builder = Uri.Builder()
        builder
                .scheme("https")
                .authority(resources.getString(R.string.api_authoritiy))
                .appendPath(resources.getString(R.string.api_path))
                .appendPath(resources.getString(R.string.api_user_path))

        if (subscribe) {
            builder.appendPath(resources.getString(R.string.api_post_add_subscription))
        }
        else {
            builder.appendPath(resources.getString(R.string.api_post_remove_subscription))
        }
        return builder.toString()
    }

    fun buildPostRankingUrl(): String {
        val builder = Uri.Builder()
        builder
                .scheme("https")
                .authority(resources.getString(R.string.api_authoritiy))
                .appendPath(resources.getString(R.string.api_path))
                .appendPath(resources.getString(R.string.api_user_path))
                .appendPath(resources.getString(R.string.api_post_rank))
        return builder.toString()
    }

    fun buildChangePassUrl(): String {
        val builder = Uri.Builder()
        builder
                .scheme("https")
                .authority(resources.getString(R.string.api_authoritiy))
                .appendPath(resources.getString(R.string.api_path))
                .appendPath(resources.getString(R.string.api_user_path))
                .appendPath(resources.getString(R.string.api_put_edit_password))
        return builder.toString()
    }

    fun buildEditUserName(): String {
        val builder = Uri.Builder()
        builder
                .scheme("https")
                .authority(resources.getString(R.string.api_authoritiy))
                .appendPath(resources.getString(R.string.api_path))
                .appendPath(resources.getString(R.string.api_user_path))
                .appendPath(resources.getString(R.string.api_put_edit_user))
        return builder.toString()
    }

    fun buildRegisterDeviceUrl(): String {
        val builder = Uri.Builder()
        builder
                .scheme("https")
                .authority(resources.getString(R.string.api_authoritiy))
                .appendPath(resources.getString(R.string.api_path))
                .appendPath(resources.getString(R.string.api_user_path))
                .appendPath(resources.getString(R.string.api_post_register_device))
        return builder.toString()
    }
}
