package com.kit.projectdesign.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Notification(
    val title: String,
    val date: String,
    val body: String
) : Parcelable
