package com.kit.projectdesign.ui.mypage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.kit.projectdesign.data.PurchaseHistoryItem
import com.kit.projectdesign.databinding.FragmentMypageBinding

class MypageFragment : Fragment() {

    private var _binding: FragmentMypageBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMypageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 1. 購入履歴のダミーデータ作成とアダプターのセットアップ
        setupPurchaseHistory()

        // 2. 開閉機能の実装
        setupToggleActions()
    }

    private fun setupPurchaseHistory() {
        val purchaseHistoryList = listOf(
            PurchaseHistoryItem("プレミアムチョコクロ", "2024/05/20"),
            PurchaseHistoryItem("じゃがバタデニッシュ", "2024/05/18"),
            PurchaseHistoryItem("塩バターパン", "2024/05/15"),
            PurchaseHistoryItem("クリームパン", "2024/05/12"),
        )

        val adapter = PurchaseHistoryAdapter(purchaseHistoryList)
        binding.recyclerViewPurchaseHistory.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewPurchaseHistory.adapter = adapter
    }

    private fun setupToggleActions() {
        // 購入履歴の開閉
        binding.headerPurchaseHistory.setOnClickListener {
            toggleVisibility(binding.contentPurchaseHistory, binding.togglePurchaseHistory)
        }

        // 作った料理の開閉
        binding.headerDishesMade.setOnClickListener {
            toggleVisibility(binding.contentDishesMade, binding.toggleDishesMade)
        }

        // いいねしたレシピの開閉
        binding.headerLikedRecipes.setOnClickListener {
            toggleVisibility(binding.contentLikedRecipes, binding.toggleLikedRecipes)
        }
    }

    // 表示・非表示を切り替え、ボタンのテキストを更新する共通の関数
    private fun toggleVisibility(content: LinearLayout, toggleButton: TextView) {
        val isVisible = content.isVisible
        content.visibility = if (isVisible) View.GONE else View.VISIBLE
        toggleButton.text = if (isVisible) "[+]" else "[-]"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
