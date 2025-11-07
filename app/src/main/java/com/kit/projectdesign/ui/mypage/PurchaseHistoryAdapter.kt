package com.kit.projectdesign.ui.mypage

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kit.projectdesign.data.PurchaseHistoryItem
import com.kit.projectdesign.databinding.ListItemPurchaseHistoryBinding

class PurchaseHistoryAdapter(
    private val items: List<PurchaseHistoryItem>
) : RecyclerView.Adapter<PurchaseHistoryAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ListItemPurchaseHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount() = items.size

    inner class ViewHolder(private val binding: ListItemPurchaseHistoryBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: PurchaseHistoryItem) {
            binding.itemName.text = item.itemName
            binding.purchaseDate.text = item.purchaseDate
        }
    }
}
