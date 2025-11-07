package com.kit.projectdesign.ui.discount

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kit.projectdesign.data.DiscountItem
import com.kit.projectdesign.databinding.ListItemDiscountBinding
import kotlin.math.roundToInt

class DiscountAdapter(
    private val items: List<DiscountItem>,
    private val onItemClicked: (DiscountItem) -> Unit
) : RecyclerView.Adapter<DiscountAdapter.DiscountViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DiscountViewHolder {
        val binding = ListItemDiscountBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DiscountViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DiscountViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item)
        holder.itemView.setOnClickListener {
            onItemClicked(item)
        }
    }

    override fun getItemCount() = items.size

    inner class DiscountViewHolder(private val binding: ListItemDiscountBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: DiscountItem) {
            binding.itemIcon.text = item.icon
            binding.itemName.text = item.name
            binding.itemNewPrice.text = "${item.newPrice}円"
            binding.itemOldPrice.text = "${item.oldPrice}円"
            binding.itemOldPrice.paintFlags = binding.itemOldPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG

            val discountPercentage = ((item.oldPrice - item.newPrice).toDouble() / item.oldPrice * 100).roundToInt()
            binding.itemDiscountPercentage.text = "$discountPercentage% OFF"

            if (item.reason != null) {
                binding.itemReason.visibility = View.VISIBLE
                binding.itemReason.text = "値引き理由：${item.reason}"
            } else {
                binding.itemReason.visibility = View.GONE
            }
        }
    }
}
