package com.kit.projectdesign.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class InstructionStep(
    val description: String,
    val imageUrl: String? // Changed from Int (resId) to String? (URL)
) : Parcelable
