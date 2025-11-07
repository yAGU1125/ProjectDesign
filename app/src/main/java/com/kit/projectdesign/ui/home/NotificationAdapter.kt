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

    // 表示する件数を保持する変数
    private var displayItemCount: Int = notifications.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder {
        val binding = ListItemNotificationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NotificationViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {
        val notification = notifications[position]
        holder.bind(notification)
        holder.itemView.setOnClickListener { 
            onItemClicked(notification)
        }
    }

    override fun getItemCount() = displayItemCount

    // 表示する件数を更新し、リストの再描画を通知するメソッド
    fun setDisplayItemCount(count: Int) {
        displayItemCount = count.coerceAtMost(notifications.size)
        notifyDataSetChanged()
    }

    inner class NotificationViewHolder(private val binding: ListItemNotificationBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(notification: Notification) {
            binding.notificationTitle.text = notification.title
            binding.notificationDate.text = notification.date
            binding.notificationBody.text = notification.body
        }
    }
}
