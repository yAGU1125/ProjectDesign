package com.kit.projectdesign.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Notification(
    val id: String = "",
    val title: String = "",
    val content: String = "",
    val date: String = "" // ISO-8601 format e.g., "2024-05-20T10:00:00"
) : Parcelable
