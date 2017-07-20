package isel.ps.ps_userclient.utils.constants

class IntentKeys {
    companion object {
        val NETWORK_RECEIVER = "NetworkReceiver"

        val DEVICE_TOKEN = "device_token"

        val USER_EMAIL="email"
        val USER_PASSWORD="password"
        val USERNAME="username"

        val SERVICE_NAME = "service_name"
        val SERVICE_ID = "service_id"
        val SERVICE = "service"
        val SERVICE_INFO = "service_info"
        val SERVICE_TYPE = "type"
        val SERVICE_LIST_TYPES = "list_types"
        val SERVICE_RESPONSE = "service_response"

        val RANKING_TEXT = "ranking_text"
        val RANKING_VALUE = "ranking_value"

        val EVENTS_INFO = "events_info"
        val EVENT_BEGIN = "event_begin"
        val EVENT_END = "event_end"
        val EVENT_TEXT = "event_text"
        val EVENT_ID = "event_id"

        val IS_SUBSCRIBING = "is_subscribing"

        val LOGIN_INFO = "login_info"
        val IS_AUTO_LOGIN = "is_auto_login"

        val SEARCH_BY_PREFERENCE = "search_with_preferences"

        val ACTION = "action"

        val ERROR = "error"

        val PAGE_REQUEST = "page"
        val SORT_ORDER = "sort_order"

        val NOTIFICATION_TITLE = "notification_title"
        val NOTIFICATION_BODY = "notification_body"
    }
}
