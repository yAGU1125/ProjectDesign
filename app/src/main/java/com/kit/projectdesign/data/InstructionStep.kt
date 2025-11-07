package com.kit.projectdesign.data

import android.os.Parcelable
import androidx.annotation.DrawableRes
import kotlinx.parcelize.Parcelize

@Parcelize
data class InstructionStep(
    val description: String,
    @DrawableRes val imageResId: Int
) : Parcelable
