package com.kit.projectdesign.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.kit.projectdesign.data.Notification
import com.kit.projectdesign.databinding.FragmentHomeBinding
import com.kit.projectdesign.R
import com.google.android.material.bottomnavigation.BottomNavigationView

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: HomeViewModel by viewModels()
    private lateinit var notificationAdapter: NotificationAdapter
    private var isExpanded = false
    private val collapsedItemCount = 4

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        observeViewModel()
        
        viewModel.fetchNotifications(requireContext())

        binding.buttonToggleNotifications.setOnClickListener {
            isExpanded = !isExpanded
            updateNotificationListVisibility()
        }
    }

    private fun setupRecyclerView() {
        notificationAdapter = NotificationAdapter(emptyList()) { notification ->
            // Mark notifications as read when one is clicked
            viewModel.markNotificationsAsRead(requireContext(), viewModel.notifications.value ?: emptyList())
            // Navigate to detail
            val action = HomeFragmentDirections.actionHomeToNotificationDetail(notification)
            findNavController().navigate(action)
        }
        binding.recyclerViewNotifications.adapter = notificationAdapter
    }

    private fun observeViewModel() {
        viewModel.notifications.observe(viewLifecycleOwner) { notifications ->
            notificationAdapter.updateNotifications(notifications)
            updateNotificationListVisibility()
        }

        viewModel.hasNewNotification.observe(viewLifecycleOwner) { hasNew ->
            val bottomNav = activity?.findViewById<BottomNavigationView>(R.id.nav_view)
            val badge = bottomNav?.getOrCreateBadge(R.id.navigation_home)
            badge?.isVisible = hasNew
        }
    }

    private fun updateNotificationListVisibility() {
        val fullListSize = viewModel.notifications.value?.size ?: 0
        if (fullListSize > collapsedItemCount) {
            binding.buttonToggleNotifications.visibility = View.VISIBLE
            if (isExpanded) {
                notificationAdapter.setDisplayItemCount(fullListSize)
                binding.buttonToggleNotifications.text = "閉じる"
            } else {
                notificationAdapter.setDisplayItemCount(collapsedItemCount)
                binding.buttonToggleNotifications.text = "もっと見る"
            }
        } else {
            notificationAdapter.setDisplayItemCount(fullListSize)
            binding.buttonToggleNotifications.visibility = View.GONE
        }
    }

    override fun onResume() {
        super.onResume()
        // Refresh data when user returns to the fragment
        viewModel.fetchNotifications(requireContext())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
