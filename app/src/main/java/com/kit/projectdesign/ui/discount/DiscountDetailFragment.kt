package com.kit.projectdesign.ui.discount

import android.graphics.Paint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.kit.projectdesign.databinding.FragmentDiscountDetailBinding
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import kotlin.math.roundToInt

class DiscountDetailFragment : Fragment() {

    private var _binding: FragmentDiscountDetailBinding? = null
    private val binding get() = _binding!!

    private val args: DiscountDetailFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDiscountDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val item = args.discountItem

        binding.detailItemName.text = "${item.icon} ${item.name}"

        binding.detailOldPrice.text = "元値：${item.oldPrice}円"
        binding.detailOldPrice.paintFlags = binding.detailOldPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG

        val discountPercentage = ((item.oldPrice - item.newPrice).toDouble() / item.oldPrice * 100).roundToInt()
        binding.detailNewPrice.text = "値引き後：${item.newPrice}円 ($discountPercentage% OFF)"

        if (item.reason != null) {
            binding.detailReason.visibility = View.VISIBLE
            binding.detailReason.text = "値引き理由：${item.reason}"
        } else {
            binding.detailReason.visibility = View.GONE
        }

        // ダミーの賞味期限を設定
        val dummyExpiryDate = LocalDate.now().plusDays(3)
        binding.detailExpiryDate.text = "賞味期限：${dummyExpiryDate.format(DateTimeFormatter.ofPattern("yyyy/MM/dd"))}"

        binding.buttonAddToCart.setOnClickListener {
            Toast.makeText(context, "${item.name}をカゴに入れました", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
