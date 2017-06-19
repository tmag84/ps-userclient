package isel.ps.ps_userclient.utils.constants

class ServiceActions {
    companion object {
        val GET_USER_SUBSCRIPTIONS = "get_subscriptions"
        val SEARCH_BY_TYPE = "search_by_type"
        val SEARCH_BY_PREFERENCES = "search_by_preference"

        val SUBSCRIBE = "subscribe"
        val UNSUBSCRIBE = "unsubscribe"

        val SUBSCRIPTIONS_REQUEST_SUCCESS : String = "SUBSCRIPTIONS_REQUEST_SUCCESS"
        val SUBSCRIPTIONS_REQUEST_FAILURE : String = "SUBSCRIPTIONS_REQUEST_FAILURE"

        val SEARCH_REQUEST_SUCCESS : String = "SEARCH_REQUEST_SUCCESS"
        val SEARCH_REQUEST_FAILURE : String = "SEARCH_REQUEST_FAILURE"
    }
}
