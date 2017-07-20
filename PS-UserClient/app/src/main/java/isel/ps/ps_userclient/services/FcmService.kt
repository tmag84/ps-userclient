package isel.ps.ps_userclient.services

import android.content.Intent
import android.support.v4.content.LocalBroadcastManager
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import isel.ps.ps_userclient.utils.constants.IntentKeys
import isel.ps.ps_userclient.utils.constants.ServiceResponses

class FcmService : FirebaseMessagingService() {
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        if (remoteMessage.notification != null) {
            val notification = remoteMessage.notification
            val intent = Intent(IntentKeys.NETWORK_RECEIVER)
            intent.putExtra(IntentKeys.SERVICE_RESPONSE, ServiceResponses.NOTIFICATION_RECEIVED)
            intent.putExtra(IntentKeys.NOTIFICATION_TITLE,notification.title)
            intent.putExtra(IntentKeys.NOTIFICATION_BODY,notification.body)
            LocalBroadcastManager.getInstance(this).sendBroadcast(intent)
        }
    }

    override fun onDeletedMessages() {
        super.onDeletedMessages()
    }
}
