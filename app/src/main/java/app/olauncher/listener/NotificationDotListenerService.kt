package app.olauncher.listener

import android.app.Notification
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import app.olauncher.data.NotificationDotStore

class NotificationDotListenerService : NotificationListenerService() {
    override fun onListenerConnected() {
        super.onListenerConnected()
        refreshActivePackages()
    }

    override fun onNotificationPosted(sbn: StatusBarNotification) {
        refreshActivePackages()
    }

    override fun onNotificationRemoved(sbn: StatusBarNotification) {
        refreshActivePackages()
    }

    private fun refreshActivePackages() {
        val packages = activeNotifications
            .orEmpty()
            .asSequence()
            .filter { it.isClearable && (it.notification.flags and Notification.FLAG_GROUP_SUMMARY) == 0 }
            .map { it.packageName }
            .toSet()

        NotificationDotStore.updateActivePackages(this, packages)
    }
}