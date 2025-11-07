package com.kit.projectdesign.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.google.android.material.chip.Chip
import com.kit.projectdesign.databinding.FragmentSettingsBinding

class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    // 選択肢となるアレルゲンのリスト
    private val allergens = listOf("卵", "乳", "小麦", "えび", "かに", "そば", "落花生", "あわび", "いか", "いくら", "オレンジ", "カシューナッツ", "キウイフルーツ", "牛肉", "くるみ", "ごま", "さけ", "さば", "大豆", "鶏肉", "バナナ", "豚肉", "まつたけ", "もも", "やまいも", "りんご", "ゼラチン")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // アレルギー選択用のChipと保存ボタンの機能を先にセットアップ
        setupAllergenChips()
        binding.saveButton.setOnClickListener {
            saveAllergySettings()
        }

        // 開閉機能のセットアップ
        binding.headerAllergy.setOnClickListener {
            toggleVisibility(binding.contentAllergy, binding.toggleAllergy)
        }
    }

    // 表示・非表示を切り替え、ボタンのテキストを更新する共通の関数
    private fun toggleVisibility(content: View, toggleButton: TextView) {
        val isVisible = content.isVisible
        content.visibility = if (isVisible) View.GONE else View.VISIBLE
        toggleButton.text = if (isVisible) "[+]" else "[-]"
    }

    private fun setupAllergenChips() {
        val chipGroup = binding.allergenChipGroup
        chipGroup.removeAllViews() // 再生成時に備えて、すでにあるChipをクリア

        for (allergen in allergens) {
            // Chipを直接生成する（安全な方法）
            val chip = Chip(requireContext())
            chip.text = allergen
            chip.isCheckable = true // チェックボックスのように選択可能にする
            chipGroup.addView(chip)
        }
    }

    private fun saveAllergySettings() {
        val selectedAllergens = mutableListOf<String>()
        val chipGroup = binding.allergenChipGroup

        for (i in 0 until chipGroup.childCount) {
            val chip = chipGroup.getChildAt(i) as Chip
            if (chip.isChecked) {
                selectedAllergens.add(chip.text.toString())
            }
        }

        val message = if (selectedAllergens.isEmpty()) {
            "アレルギー情報は設定されていません。"
        } else {
            "保存されたアレルギー情報: ${selectedAllergens.joinToString()}"
        }

        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()

        // TODO: 取得したselectedAllergensのリストを実際に保存する処理（例: SharedPreferences）を今後実装
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
