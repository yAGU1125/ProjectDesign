package com.kit.projectdesign.util

import android.content.Context
import android.content.SharedPreferences

object NotificationReadPrefs {

    private const val PREFS_NAME = "notification_read_prefs"
    private const val KEY_READ_IDS = "read_notification_ids"

    private fun getPrefs(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    // 既読のIDセットを取得する
    fun getReadNotificationIds(context: Context): Set<String> {
        return getPrefs(context).getStringSet(KEY_READ_IDS, emptySet()) ?: emptySet()
    }

    // IDを既読として保存する
    fun addReadNotificationId(context: Context, id: String) {
        val prefs = getPrefs(context)
        val readIds = getReadNotificationIds(context).toMutableSet()
        readIds.add(id)
        prefs.edit().putStringSet(KEY_READ_IDS, readIds).apply()
    }
}
