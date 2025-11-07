package com.kit.projectdesign.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class DiscountItem(
    val name: String,
    val oldPrice: Int,
    val newPrice: Int,
    val reason: String? = null, // 任意項目
    val icon: String // プレースホルダーとして絵文字などを利用
) : Parcelable
