package com.kit.projectdesign.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Notification(
    val id: String, // お知らせを一位に識別するためのID
    val title: String,
    val date: String,
    val body: String,
    var isRead: Boolean = false
) : Parcelable
