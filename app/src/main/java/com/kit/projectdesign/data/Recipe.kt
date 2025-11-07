package com.kit.projectdesign.data

import android.os.Parcelable
import androidx.annotation.DrawableRes
import com.kit.projectdesign.data.InstructionStep
import kotlinx.parcelize.Parcelize

@Parcelize
data class Recipe(
    // --- 一覧表示用の基本情報 ---
    val name: String,
    val description: String,
    @DrawableRes val imageResId: Int,
    var isLiked: Boolean = false, // いいねの状態を追加

    // --- 詳細表示用の追加情報 ---
    val servings: String, // 〜人分
    val ingredients: List<String>, // 食材
    val seasonings: List<String>, // 調味料
    val instructions: List<InstructionStep>, //作り方
    val tips: List<String> // コツ・ポイント
) : Parcelable
