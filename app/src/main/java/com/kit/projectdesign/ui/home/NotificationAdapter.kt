package com.kit.projectdesign.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kit.projectdesign.data.Notification
import com.kit.projectdesign.databinding.ListItemNotificationBinding

class NotificationAdapter(
    private var notifications: List<Notification>,
    private val onItemClicked: (Notification) -> Unit
) : RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder>() {

    private var displayItemCount = notifications.size

    fun setDisplayItemCount(count: Int) {
        displayItemCount = count
        notifyDataSetChanged()
    }

    fun updateNotifications(newNotifications: List<Notification>) {
        notifications = newNotifications
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder {
        val binding = ListItemNotificationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NotificationViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {
        holder.bind(notifications[position])
    }

    override fun getItemCount() = displayItemCount

    inner class NotificationViewHolder(private val binding: ListItemNotificationBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(notification: Notification) {
            binding.notificationTitle.text = notification.title
            binding.notificationDate.text = try {
                // Format date for better readability if needed
                notification.date.substring(0, 10)
            } catch (e: Exception) {
                notification.date
            }
            itemView.setOnClickListener {
                onItemClicked(notification)
            }
        }
    }
}
