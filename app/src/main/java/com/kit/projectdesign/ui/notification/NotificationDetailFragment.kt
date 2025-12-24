package com.kit.projectdesign.ui.notification

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.kit.projectdesign.databinding.FragmentNotificationDetailBinding

class NotificationDetailFragment : Fragment() {

    private var _binding: FragmentNotificationDetailBinding? = null
    private val binding get() = _binding!!

    private val args: NotificationDetailFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNotificationDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val notification = args.notification

        // データをシンプルなViewに設定
        binding.notificationDetailTitle.text = notification.title
        binding.notificationDetailDate.text = notification.date
        binding.notificationDetailBody.text = notification.content // Changed from .body to .content

        // アプリ全体のツールバーにタイトルを設定
        (activity as? AppCompatActivity)?.supportActionBar?.title = notification.title
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
