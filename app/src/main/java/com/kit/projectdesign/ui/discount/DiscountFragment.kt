package com.kit.projectdesign.ui.discount

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.kit.projectdesign.databinding.FragmentDiscountBinding

class DiscountFragment : Fragment() {

    private var _binding: FragmentDiscountBinding? = null
    private val binding get() = _binding!!

    private val viewModel: DiscountViewModel by viewModels()

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

        // RecyclerView Setup
        binding.recyclerViewDiscount.layoutManager = LinearLayoutManager(context)
        val adapter = DiscountAdapter(emptyList()) { item ->
            val action = DiscountFragmentDirections.actionNavigationDiscountToNavigationDiscountDetail(item)
            findNavController().navigate(action)
        }
        binding.recyclerViewDiscount.adapter = adapter

        // Observe ViewModel
        viewModel.discountItems.observe(viewLifecycleOwner) { items ->
            // Update adapter with new list.
            // Note: Since DiscountAdapter is simple, we might need to create a new one or add a method to update data.
            // A better approach is to add a submitList method to the adapter or use ListAdapter.
            // For now, I will re-instantiate the adapter or just let me check the adapter code again.
            // DiscountAdapter takes items in constructor. I should modify DiscountAdapter to allow updating items.
             binding.recyclerViewDiscount.adapter = DiscountAdapter(items) { item ->
                val action = DiscountFragmentDirections.actionNavigationDiscountToNavigationDiscountDetail(item)
                findNavController().navigate(action)
            }
        }

        viewModel.error.observe(viewLifecycleOwner) { errorMessage ->
            if (errorMessage != null) {
                Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
            }
        }

        // Fetch data
        viewModel.fetchDiscountItems()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
