package com.kit.projectdesign.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.kit.projectdesign.data.Notification
import com.kit.projectdesign.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var notificationAdapter: NotificationAdapter
    private lateinit var fullNotificationList: List<Notification>
    private var isExpanded = false
    private val collapsedItemCount = 4 // 表示件数を4件に変更

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
        fullNotificationList = listOf(
            Notification("新しいレシピを追加しました！", "2024/05/22", "期間限定のプレミアムチョコクロワッサンのレシピを公開しました。ぜひチェックしてみてください！\n\n材料や作り方の詳細はこちらからご覧いただけます。お家で本格的な味を再現してみましょう！"),
            Notification("メンテナンスのお知らせ", "2024/05/20", "本日23:00より、サーバーメンテナンスを実施します。ご不便をおかけしますが、ご理解とご協力をお願いいたします。"),
            Notification("アプリバージョンアップのお知らせ", "2024/05/18", "新しい機能を追加したバージョン2.0をリリースしました。最新の機能をお楽しみいただくために、ストアからアップデートしてください。"),
            Notification("割引クーポンプレゼント！", "2024/05/15", "いつもご利用ありがとうございます。本日限定で利用できる10%OFFクーポンをプレゼントします！マイページからご確認ください。"),
            Notification("ようこそ！", "2024/05/10", "ProjectDesignへようこそ！あなたの毎日が、もっと豊かになりますように。"),
            Notification("【重要】利用規約改定のお知らせ", "2024/05/09", "2024年6月1日より、利用規約を一部改定いたします。詳細については、マイページのお知らせをご確認ください。"),
            Notification("季節限定の新メニュー登場！", "2024/05/05", "春の訪れを感じる、桜風味のパンが新登場！ぜひご賞味ください。"),
            Notification("友達招待キャンペーン開始", "2024/05/01", "お友達を招待すると、あなたとお友達の両方に500円分のポイントをプレゼント！詳細はキャンペーンページをご覧ください。" )
        )

        // Adapterのセットアップ
        notificationAdapter = NotificationAdapter(fullNotificationList) { notification ->
            val action = HomeFragmentDirections.actionHomeToNotificationDetail(notification)
            findNavController().navigate(action)
        }
        binding.recyclerViewNotifications.adapter = notificationAdapter

        // 初期表示とボタンの表示制御
        if (fullNotificationList.size > collapsedItemCount) {
            notificationAdapter.setDisplayItemCount(collapsedItemCount)
            binding.buttonToggleNotifications.visibility = View.VISIBLE
        } else {
            notificationAdapter.setDisplayItemCount(fullNotificationList.size)
            binding.buttonToggleNotifications.visibility = View.GONE
        }

        // ボタンのクリックリスナー
        binding.buttonToggleNotifications.setOnClickListener {
            isExpanded = !isExpanded
            if (isExpanded) {
                notificationAdapter.setDisplayItemCount(fullNotificationList.size)
                binding.buttonToggleNotifications.text = "閉じる"
            } else {
                notificationAdapter.setDisplayItemCount(collapsedItemCount)
                binding.buttonToggleNotifications.text = "もっと見る"
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
