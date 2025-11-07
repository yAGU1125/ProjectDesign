package com.kit.projectdesign.ui.recipe

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.kit.projectdesign.R
import com.kit.projectdesign.data.InstructionStep
import com.kit.projectdesign.data.Recipe
import com.kit.projectdesign.databinding.FragmentRecipeBinding

class RecipeFragment : Fragment() {

    private var _binding: FragmentRecipeBinding? = null
    private val binding get() = _binding!!

    private lateinit var recipeAdapter: RecipeAdapter
    private lateinit var recipeList: MutableList<Recipe>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRecipeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // ダミーデータを作成
        setupRecipeList()

        // Adapterのセットアップ
        recipeAdapter = RecipeAdapter(
            recipes = recipeList,
            onItemClicked = { recipe ->
                val action = RecipeFragmentDirections.actionRecipeToDetail(recipe)
                findNavController().navigate(action)
            },
            onLikeClicked = { recipe ->
                handleLikeClick(recipe)
            }
        )
        binding.recyclerViewRecipe.adapter = recipeAdapter
    }

    private fun handleLikeClick(clickedRecipe: Recipe) {
        // 1. いいねの状態を反転させる
        clickedRecipe.isLiked = !clickedRecipe.isLiked

        // 2. アダプターにデータの変更を通知する
        val position = recipeList.indexOf(clickedRecipe)
        if (position != -1) {
            recipeAdapter.notifyItemChanged(position)
        }

        // 3. いいねされた場合にSnackbarでメッセージを表示
        if (clickedRecipe.isLiked) {
            val message = "「${clickedRecipe.name}」はいいねしたレシピに保存されました。"
            Snackbar.make(binding.root, message, Snackbar.LENGTH_LONG)
                .setDuration(3000) // 3秒で消えるように設定
                .show()
        }
    }

    private fun setupRecipeList() {
        // (ダミーデータは長いため、ここでは省略。以前の内容が維持されます)
        recipeList = mutableListOf(
             Recipe(
                name = "プレミアムチョコクロ",
                description = "チョコクロにスライスアーモンドとアーモンドクリームをトッピングしてカリッと香ばしく焼き上げました。",
                imageResId = R.drawable.ic_recipe,
                servings = "2個分",
                ingredients = listOf("強力粉: 100g", "薄力粉: 50g", "板チョコレート: 50g", "スライスアーモンド: 適量"),
                seasonings = listOf("砂糖: 大さじ2", "塩: 少々", "無塩バター: 20g", "卵黄: 1個分"),
                instructions = listOf(
                    InstructionStep("ボウルに粉類と砂糖、塩を入れ、冷たいバターを加えて混ぜ合わせます。", R.drawable.ic_recipe),
                    InstructionStep("水を少しずつ加え、ひとまとめにして冷蔵庫で30分休ませます。", R.drawable.ic_recipe),
                    InstructionStep("生地を伸ばし、チョコレートを包んで三日月形に成形します。", R.drawable.ic_recipe),
                    InstructionStep("表面に卵黄を塗り、スライスアーモンドを散らして、180℃のオーブンで15分焼きます。", R.drawable.ic_recipe)
                ),
                tips = listOf("バターは冷たいまま使うのがサクサクの秘訣です。", "焼きたてはもちろん、冷めても美味しくいただけます。")
            ),
            Recipe(
                name = "じゃがバタデニッシュ",
                description = "サクサクの生地にじゃがいもとベーコンをトッピング！軽い食事としてどうぞ。",
                imageResId = R.drawable.ic_recipe,
                servings = "1個分",
                ingredients = listOf("冷凍パイシート: 1枚", "じゃがいも: 1個", "ベーコン: 2枚"),
                seasonings = listOf("バター: 10g", "マヨネーズ: 大さじ1", "黒胡椒: 少々"),
                instructions = listOf(
                    InstructionStep("じゃがいもは皮をむいて薄切りにし、電子レンジで柔らかくなるまで加熱します。", R.drawable.ic_recipe),
                    InstructionStep("パイシートにマヨネーズを塗り、じゃがいもとベーコンを乗せます。", R.drawable.ic_recipe),
                    InstructionStep("上にバターを乗せ、200℃のオーブンで20分焼きます。", R.drawable.ic_recipe)
                ),
                tips = listOf("お好みでチーズを乗せても美味しいです。")
            ),
            Recipe(
                name = "目玉焼きデニッシュ",
                description = "サクサクの生地に目玉焼きをドーンっとトッピング。",
                imageResId = R.drawable.ic_recipe,
                servings = "1個分",
                ingredients = listOf("冷凍パイシート: 1枚", "卵: 1個"),
                seasonings = listOf("ケチャップ: 適量", "パセリ: 少々"),
                instructions = listOf(
                    InstructionStep("パイシートの縁を少し残して中央をフォークで刺します。", R.drawable.ic_recipe),
                    InstructionStep("中央に卵を割り入れ、黄身が崩れないようにします。", R.drawable.ic_recipe),
                    InstructionStep("200℃のオーブンで15分ほど、パイが膨らみ、卵白が固まるまで焼きます。", R.drawable.ic_recipe),
                    InstructionStep("仕上げにケチャップとパセリをかけます。", R.drawable.ic_recipe)
                ),
                tips = listOf("黄身を半熟に仕上げるのがポイントです。")
            ),
            Recipe(
                name = "フレンチトースト",
                description = "自家製フレンチトースト液をたっぷり染み込ませました。",
                imageResId = R.drawable.ic_recipe,
                servings = "2人分",
                ingredients = listOf("食パン(6枚切り): 2枚", "卵: 1個", "牛乳: 150ml"),
                seasonings = listOf("砂糖: 大さじ1", "バター: 10g", "メープルシロップ: お好みで"),
                instructions = listOf(
                    InstructionStep("ボウルに卵、牛乳、砂糖を入れてよく混ぜ合わせ、卵液を作ります。", R.drawable.ic_recipe),
                    InstructionStep("食パンを卵液に浸し、両面にしっかりと染み込ませます。", R.drawable.ic_recipe),
                    InstructionStep("フライパンにバターを熱し、弱火でパンの両面をじっくりと焼きます。", R.drawable.ic_recipe),
                    InstructionStep("焼き色がついたら皿に盛り付け、お好みでメープルシロップをかけます。", R.drawable.ic_recipe)
                ),
                tips = listOf("パンを卵液に一晩浸しておくと、さらにふわふわになります。")
            ),
            Recipe(
                name = "塩バターパン",
                description = "ジュワっと溶け出すバターと、岩塩のアクセントがたまらない一品です。",
                imageResId = R.drawable.ic_recipe,
                servings = "4個分",
                ingredients = listOf("強力粉: 200g", "有塩バター: 30g", "岩塩: 少々"),
                seasonings = listOf("砂糖: 大さじ1", "ドライイースト: 3g"),
                instructions = listOf(
                    InstructionStep("材料をすべて混ぜてこね、一次発酵させます。", R.drawable.ic_recipe),
                    InstructionStep("ガス抜きをして4等分し、丸めてベンチタイムを取ります。", R.drawable.ic_recipe),
                    InstructionStep("生地を伸ばしてバターを包み、岩塩を振って二次発酵させます。", R.drawable.ic_recipe),
                    InstructionStep("190℃のオーブンで12分焼きます。", R.drawable.ic_recipe)
                ),
                tips = listOf("焼く直前に霧吹きで水をかけると、表面がパリッとします。")
            ),
            Recipe(
                name = "ベーコンエピ",
                description = "麦の穂の形をした、見た目もおしゃれなフランスパン。ベーコンの旨味がたっぷりです。",
                imageResId = R.drawable.ic_recipe,
                servings = "2本分",
                ingredients = listOf("フランスパン専用粉: 250g", "ベーコン: 4枚"),
                seasonings = listOf("塩: 4g", "ドライイースト: 2g", "黒胡椒: 適量"),
                instructions = listOf(
                    InstructionStep("生地をこねて一次発酵させます。", R.drawable.ic_recipe),
                    InstructionStep("生地を伸ばしてベーコンと黒胡椒を乗せ、棒状に巻きます。", R.drawable.ic_recipe),
                    InstructionStep("ハサミで斜めに切り込みを入れ、交互にずらして穂の形にします。", R.drawable.ic_recipe),
                    InstructionStep("220℃のオーブンで20分焼きます。", R.drawable.ic_recipe)
                ),
                tips = listOf("切り込みを深く入れると、火の通りが良くなりカリカリになります。")
            ),
            Recipe(
                name = "クリームパン",
                description = "自家製のなめらかカスタードクリームがたっぷり入った、昔ながらの優しい味。",
                imageResId = R.drawable.ic_recipe,
                servings = "5個分",
                ingredients = listOf("強力粉: 200g", "卵黄: 2個分", "牛乳: 200ml", "薄力粉: 20g"),
                seasonings = listOf("砂糖: 60g", "バニラエッセンス: 少々"),
                instructions = listOf(
                    InstructionStep("カスタードクリームの材料を鍋で混ぜ合わせ、とろみがつくまで加熱します。", R.drawable.ic_recipe),
                    InstructionStep("パン生地をこねて一次発酵させ、5等分します。", R.drawable.ic_recipe),
                    InstructionStep("生地を丸く伸ばし、冷ましたカスタードを包みます。", R.drawable.ic_recipe),
                    InstructionStep("二次発酵させた後、表面に卵黄を塗り、180℃のオーブンで15分焼きます。", R.drawable.ic_recipe)
                ),
                tips = listOf("カスタードを包むときは、生地をしっかりと閉じるのがポイントです。")
            )
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
