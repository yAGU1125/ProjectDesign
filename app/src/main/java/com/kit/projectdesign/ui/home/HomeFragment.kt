package com.kit.projectdesign.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.kit.projectdesign.data.Notification
import com.kit.projectdesign.databinding.FragmentHomeBinding
import com.kit.projectdesign.util.NotificationReadPrefs

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var notificationAdapter: NotificationAdapter
    private lateinit var fullNotificationList: MutableList<Notification>
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

        // ダミーデータを作成
        setupDummyData()

        // 既読状態を読み込んで反映
        loadAndApplyReadStatus()

        // Adapterのセットアップ
        notificationAdapter = NotificationAdapter(fullNotificationList) { notification ->
            // タップされたお知らせを既読にする
            markAsRead(notification)
            
            // 詳細画面へ遷移
            val action = HomeFragmentDirections.actionHomeToNotificationDetail(notification)
            findNavController().navigate(action)
        }
        binding.recyclerViewNotifications.adapter = notificationAdapter

        // 初期表示とボタンの表示制御
        updateNotificationListVisibility()

        // ボタンのクリックリスナー
        binding.buttonToggleNotifications.setOnClickListener {
            isExpanded = !isExpanded
            updateNotificationListVisibility()
        }
    }

    private fun markAsRead(notification: Notification) {
        if (!notification.isRead) {
            notification.isRead = true
            // 既読IDを永続保存
            NotificationReadPrefs.addReadNotificationId(requireContext(), notification.id)

            val index = fullNotificationList.indexOf(notification)
            if (index != -1) {
                notificationAdapter.notifyItemChanged(index)
            }
        }
    }

    private fun loadAndApplyReadStatus() {
        val readIds = NotificationReadPrefs.getReadNotificationIds(requireContext())
        for (notification in fullNotificationList) {
            if (readIds.contains(notification.id)) {
                notification.isRead = true
            }
        }
    }

    private fun updateNotificationListVisibility() {
        if (fullNotificationList.size > collapsedItemCount) {
            binding.buttonToggleNotifications.visibility = View.VISIBLE
            if (isExpanded) {
                notificationAdapter.setDisplayItemCount(fullNotificationList.size)
                binding.buttonToggleNotifications.text = "閉じる"
            } else {
                notificationAdapter.setDisplayItemCount(collapsedItemCount)
                binding.buttonToggleNotifications.text = "もっと見る"
            }
        } else {
            notificationAdapter.setDisplayItemCount(fullNotificationList.size)
            binding.buttonToggleNotifications.visibility = View.GONE
        }
    }

    private fun setupDummyData() {
        // IDを持つ新しいデータ構造に合わせる
        fullNotificationList = mutableListOf(
            Notification("1", "新しいレシピを追加しました！", "2024/05/22", "期間限定のプレミアムチョコクロワッサンのレシピを公開しました。ぜひチェックしてみてください！"),
            Notification("2", "メンテナンスのお知らせ", "2024/05/20", "本日23:00より、サーバーメンテナンスを実施します。ご不便をおかけしますが、ご理解とご協力をお願いいたします。"),
            Notification("3", "アプリバージョンアップのお知らせ", "2024/05/18", "新しい機能を追加したバージョン2.0をリリースしました。最新の機能をお楽しみいただくために、ストアからアップデートしてください。"),
            Notification("4", "割引クーポンプレゼント！", "2024/05/15", "いつもご利用ありがとうございます。本日限定で利用できる10%OFFクーポンをプレゼントします！マイページからご確認ください。"),
            Notification("5", "ようこそ！", "2024/05/10", "ProjectDesignへようこそ！あなたの毎日が、もっと豊かになりますように。"),
            Notification("6", "【重要】利用規約改定のお知らせ", "2024/05/09", "2024年6月1日より、利用規約を一部改定いたします。詳細については、マイページのお知らせをご確認ください。"),
            Notification("7", "季節限定の新メニュー登場！", "2024/05/05", "春の訪れを感じる、桜風味のパンが新登場！ぜひご賞味ください。"),
            Notification("8", "友達招待キャンペーン開始", "2024/05/01", "お友達を招待すると、あなたとお友達の両方に500円分のポイントをプレゼント！詳細はキャンペーンページをご覧ください。" )
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
