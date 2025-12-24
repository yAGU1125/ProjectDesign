package com.kit.projectdesign.ui.mypage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.kit.projectdesign.databinding.FragmentMypageBinding

class MypageFragment : Fragment() {

    private var _binding: FragmentMypageBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMypageBinding.inflate(inflater, container, false)
        val root: View = binding.root

        setupExpandableSection(binding.headerPurchaseHistory, binding.togglePurchaseHistory, binding.contentPurchaseHistory)
        setupExpandableSection(binding.headerDishesMade, binding.toggleDishesMade, binding.contentDishesMade)
        setupExpandableSection(binding.headerLikedRecipes, binding.toggleLikedRecipes, binding.contentLikedRecipes)

        return root
    }

    private fun setupExpandableSection(header: View, toggleIcon: View, content: View) {
        header.setOnClickListener {
            if (content.visibility == View.VISIBLE) {
                content.visibility = View.GONE
                (toggleIcon as? android.widget.TextView)?.text = "[+]"
            } else {
                content.visibility = View.VISIBLE
                (toggleIcon as? android.widget.TextView)?.text = "[-]"
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
