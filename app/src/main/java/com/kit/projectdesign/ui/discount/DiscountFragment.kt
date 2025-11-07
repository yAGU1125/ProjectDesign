package com.kit.projectdesign.ui.discount

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.kit.projectdesign.data.DiscountItem
import com.kit.projectdesign.databinding.FragmentDiscountBinding

class DiscountFragment : Fragment() {

    private var _binding: FragmentDiscountBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDiscountBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // ダミーデータの作成
        val discountItems = listOf(
            DiscountItem("サンドイッチ", 350, 240, "消費期限が近いため", "🥪"),
            DiscountItem("からあげ弁当", 580, 400, "夕方特価セール", "🍱"),
            DiscountItem("食パン", 150, 120, null, "🍞"),
            DiscountItem("牛乳", 210, 150, "パッケージデザイン変更のため", "🥛"),
            DiscountItem("リンゴ", 98, 70, "豊作による特別価格", "🍎")
        )

        // RecyclerViewにアダプターとレイアウトマネージャーをセット
        binding.recyclerViewDiscount.layoutManager = LinearLayoutManager(context)
        val adapter = DiscountAdapter(discountItems) { item ->
            // アイテムがクリックされたときの処理
            val action = DiscountFragmentDirections.actionNavigationDiscountToNavigationDiscountDetail(item)
            findNavController().navigate(action)
        }
        binding.recyclerViewDiscount.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
