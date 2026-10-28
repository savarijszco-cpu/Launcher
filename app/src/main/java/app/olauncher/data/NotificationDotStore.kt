package app.olauncher.data

import android.content.Context

object NotificationDotStore {
    const val PREFERENCES_NAME = "notification_dots"
    const val ACTIVE_PACKAGES_KEY = "active_packages"

    fun hasActiveNotification(context: Context, packageName: String): Boolean =
        activePackages(context).contains(packageName)

    fun activePackages(context: Context): Set<String> = context.getSharedPreferences(
        PREFERENCES_NAME,
        Context.MODE_PRIVATE
    ).getStringSet(ACTIVE_PACKAGES_KEY, emptySet()).orEmpty()

    fun updateActivePackages(context: Context, packages: Set<String>) {
        context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)
            .edit()
            .putStringSet(ACTIVE_PACKAGES_KEY, packages.toSet())
            .apply()
    }
}