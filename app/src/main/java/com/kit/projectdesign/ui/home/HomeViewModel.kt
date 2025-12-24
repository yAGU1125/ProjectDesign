package com.kit.projectdesign.ui.home

import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kit.projectdesign.api.ApiClient
import com.kit.projectdesign.data.Notification
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {

    private val _notifications = MutableLiveData<List<Notification>>()
    val notifications: LiveData<List<Notification>> = _notifications

    private val _hasNewNotification = MutableLiveData<Boolean>()
    val hasNewNotification: LiveData<Boolean> = _hasNewNotification

    fun fetchNotifications(context: Context) {
        viewModelScope.launch {
            try {
                val response = ApiClient.service.getNotifications()
                if (response.isSuccessful && response.body() != null) {
                    val remoteNotifications = response.body()!!
                    _notifications.value = remoteNotifications

                    // Check for new notifications
                    checkForNewNotifications(context, remoteNotifications)
                }
            } catch (e: Exception) {
                // Handle error
            }
        }
    }

    private fun checkForNewNotifications(context: Context, notifications: List<Notification>) {
        if (notifications.isEmpty()) {
            _hasNewNotification.value = false
            return
        }

        val prefs = context.getSharedPreferences("notification_prefs", Context.MODE_PRIVATE)
        val lastReadId = prefs.getString("last_read_notification_id", null)
        
        val latestRemoteId = notifications.first().id
        
        if (latestRemoteId != lastReadId) {
            _hasNewNotification.value = true
        } else {
            _hasNewNotification.value = false
        }
    }

    fun markNotificationsAsRead(context: Context, notifications: List<Notification>) {
        if (notifications.isNotEmpty()) {
            val latestId = notifications.first().id
            val prefs = context.getSharedPreferences("notification_prefs", Context.MODE_PRIVATE)
            prefs.edit().putString("last_read_notification_id", latestId).apply()
            _hasNewNotification.value = false // Hide indicator
        }
    }
}
