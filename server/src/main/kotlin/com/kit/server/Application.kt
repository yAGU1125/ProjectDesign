package com.kit.server

import io.ktor.serialization.gson.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.http.*
import java.util.UUID

// Data Classes
data class DiscountItem(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val oldPrice: Int,
    val newPrice: Int,
    val reason: String? = null,
    val icon: String
)

data class InstructionStep(
    val description: String,
    val imageUrl: String?
)

data class Recipe(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val description: String,
    val imageUrl: String?,
    val servings: String,
    val ingredients: List<String>,
    val seasonings: List<String>,
    val instructions: List<InstructionStep>,
    val tips: List<String>
)

// In-memory storage (Global variables)
val discountItems = mutableListOf(
    DiscountItem(name = "サンドイッチ", oldPrice = 350, newPrice = 240, reason = "消費期限が近いため", icon = "🥪"),
    DiscountItem(name = "からあげ弁当", oldPrice = 580, newPrice = 400, reason = "夕方特価セール", icon = "🍱"),
    DiscountItem(name = "食パン", oldPrice = 150, newPrice = 120, reason = null, icon = "🍞"),
    DiscountItem(name = "牛乳", oldPrice = 210, newPrice = 150, reason = "パッケージデザイン変更のため", icon = "🥛"),
    DiscountItem(name = "リンゴ", oldPrice = 98, newPrice = 70, reason = "豊作による特別価格", icon = "🍎")
)

val recipes = mutableListOf(
    Recipe(
        name = "プレミアムチョコクロ",
        description = "チョコクロにスライスアーモンドとアーモンドクリームをトッピングしてカリッと香ばしく焼き上げました。",
        imageUrl = null,
        servings = "2個分",
        ingredients = listOf("強力粉: 100g", "薄力粉: 50g", "板チョコレート: 50g", "スライスアーモンド: 適量"),
        seasonings = listOf("砂糖: 大さじ2", "塩: 少々", "無塩バター: 20g", "卵黄: 1個分"),
        instructions = listOf(
            InstructionStep("ボウルに粉類と砂糖、塩を入れ、冷たいバターを加えて混ぜ合わせます。", null),
            InstructionStep("水を少しずつ加え、ひとまとめにして冷蔵庫で30分休ませます。", null),
            InstructionStep("生地を伸ばし、チョコレートを包んで三日月形に成形します。", null),
            InstructionStep("表面に卵黄を塗り、スライスアーモンドを散らして、180℃のオーブンで15分焼きます。", null)
        ),
        tips = listOf("バターは冷たいまま使うのがサクサクの秘訣です。", "焼きたてはもちろん、冷めても美味しくいただけます。")
    ),
    Recipe(
        name = "じゃがバタデニッシュ",
        description = "サクサクの生地にじゃがいもとベーコンをトッピング！軽い食事としてどうぞ。",
        imageUrl = null,
        servings = "1個分",
        ingredients = listOf("冷凍パイシート: 1枚", "じゃがいも: 1個", "ベーコン: 2枚"),
        seasonings = listOf("バター: 10g", "マヨネーズ: 大さじ1", "黒胡椒: 少々"),
        instructions = listOf(
            InstructionStep("じゃがいもは皮をむいて薄切りにし、電子レンジで柔らかくなるまで加熱します。", null),
            InstructionStep("パイシートにマヨネーズを塗り、じゃがいもとベーコンを乗せます。", null),
            InstructionStep("上にバターを乗せ、200℃のオーブンで20分焼きます。", null)
        ),
        tips = listOf("お好みでチーズを乗せても美味しいです。")
    ),
    Recipe(
        name = "目玉焼きデニッシュ",
        description = "サクサクの生地に目玉焼きをドーンっとトッピング。",
        imageUrl = null,
        servings = "1個分",
        ingredients = listOf("冷凍パイシート: 1枚", "卵: 1個"),
        seasonings = listOf("ケチャップ: 適量", "パセリ: 少々"),
        instructions = listOf(
            InstructionStep("パイシートの縁を少し残して中央をフォークで刺します。", null),
            InstructionStep("中央に卵を割り入れ、黄身が崩れないようにします。", null),
            InstructionStep("200℃のオーブンで15分ほど、パイが膨らみ、卵白が固まるまで焼きます。", null),
            InstructionStep("仕上げにケチャップとパセリをかけます。", null)
        ),
        tips = listOf("黄身を半熟に仕上げるのがポイントです。")
    ),
    Recipe(
        name = "フレンチトースト",
        description = "自家製フレンチトースト液をたっぷり染み込ませました。",
        imageUrl = null,
        servings = "2人分",
        ingredients = listOf("食パン(6枚切り): 2枚", "卵: 1個", "牛乳: 150ml"),
        seasonings = listOf("砂糖: 大さじ1", "バター: 10g", "メープルシロップ: お好みで"),
        instructions = listOf(
            InstructionStep("ボウルに卵、牛乳、砂糖を入れてよく混ぜ合わせ、卵液を作ります。", null),
            InstructionStep("食パンを卵液に浸し、両面にしっかりと染み込ませます。", null),
            InstructionStep("フライパンにバターを熱し、弱火でパンの両面をじっくりと焼きます。", null),
            InstructionStep("焼き色がついたら皿に盛り付け、お好みでメープルシロップをかけます。", null)
        ),
        tips = listOf("パンを卵液に一晩浸しておくと、さらにふわふわになります。")
    ),
    Recipe(
        name = "塩バターパン",
        description = "ジュワっと溶け出すバターと、岩塩のアクセントがたまらない一品です。",
        imageUrl = null,
        servings = "4個分",
        ingredients = listOf("強力粉: 200g", "有塩バター: 30g", "岩塩: 少々"),
        seasonings = listOf("砂糖: 大さじ1", "ドライイースト: 3g"),
        instructions = listOf(
            InstructionStep("材料をすべて混ぜてこね、一次発酵させます。", null),
            InstructionStep("ガス抜きをして4等分し、丸めてベンチタイムを取ります。", null),
            InstructionStep("生地を伸ばしてバターを包み、岩塩を振って二次発酵させます。", null),
            InstructionStep("190℃のオーブンで12分焼きます。", null)
        ),
        tips = listOf("焼く直前に霧吹きで水をかけると、表面がパリッとします。")
    ),
    Recipe(
        name = "ベーコンエピ",
        description = "麦の穂の形をした、見た目もおしゃれなフランスパン。ベーコンの旨味がたっぷりです。",
        imageUrl = null,
        servings = "2本分",
        ingredients = listOf("フランスパン専用粉: 250g", "ベーコン: 4枚"),
        seasonings = listOf("塩: 4g", "ドライイースト: 2g", "黒胡椒: 適量"),
        instructions = listOf(
            InstructionStep("生地をこねて一次発酵させます。", null),
            InstructionStep("生地を伸ばしてベーコンと黒胡椒を乗せ、棒状に巻きます。", null),
            InstructionStep("ハサミで斜めに切り込みを入れ、交互にずらして穂の形にします。", null),
            InstructionStep("220℃のオーブンで20分焼きます。", null)
        ),
        tips = listOf("切り込みを深く入れると、火の通りが良くなりカリカリになります。")
    ),
    Recipe(
        name = "クリームパン",
        description = "自家製のなめらかカスタードクリームがたっぷり入った、昔ながらの優しい味。",
        imageUrl = null,
        servings = "5個分",
        ingredients = listOf("強力粉: 200g", "卵黄: 2個分", "牛乳: 200ml", "薄力粉: 20g"),
        seasonings = listOf("砂糖: 60g", "バニラエッセンス: 少々"),
        instructions = listOf(
            InstructionStep("カスタードクリームの材料を鍋で混ぜ合わせ、とろみがつくまで加熱します。", null),
            InstructionStep("パン生地をこねて一次発酵させ、5等分します。", null),
            InstructionStep("生地を丸く伸ばし、冷ましたカスタードを包みます。", null),
            InstructionStep("二次発酵させた後、表面に卵黄を塗り、180℃のオーブンで15分焼きます。", null)
        ),
        tips = listOf("カスタードを包むときは、生地をしっかりと閉じるのがポイントです。")
    )
)

fun main() {
    // Port set to 3031 as requested
    embeddedServer(Netty, port = 3031, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    install(ContentNegotiation) {
        gson {
            setPrettyPrinting()
        }
    }

    routing {
        get("/") {
            call.respondText("Server is running on port 3031. Visit /admin to manage content.")
        }

        // --- Discounts API ---
        route("/discounts") {
            get {
                call.respond(discountItems)
            }
            post {
                try {
                    val item = call.receive<DiscountItem>()
                    // Use a new ID or the one provided
                    val newItem = if (item.id.isEmpty()) item.copy(id = UUID.randomUUID().toString()) else item
                    discountItems.add(0, newItem) // Add to top
                    call.respond(HttpStatusCode.Created, newItem)
                } catch (e: Exception) {
                    call.respond(HttpStatusCode.BadRequest, "Invalid data: ${e.message}")
                }
            }
        }

        // --- Recipes API ---
        route("/recipes") {
            get {
                call.respond(recipes)
            }
            post {
                try {
                    val item = call.receive<Recipe>()
                    val newItem = if (item.id.isEmpty()) item.copy(id = UUID.randomUUID().toString()) else item
                    recipes.add(0, newItem) // Add to top
                    call.respond(HttpStatusCode.Created, newItem)
                } catch (e: Exception) {
                    call.respond(HttpStatusCode.BadRequest, "Invalid data: ${e.message}")
                }
            }
        }

        // --- Admin Web Page ---
        get("/admin") {
            call.respondText(adminHtml, ContentType.Text.Html)
        }
    }
}

// Simple Admin HTML embedded for convenience
val adminHtml = """
<!DOCTYPE html>
<html lang="ja">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Project Design Admin</title>
    <style>
        body { font-family: sans-serif; max-width: 800px; margin: 0 auto; padding: 20px; }
        .card { border: 1px solid #ccc; padding: 20px; margin-bottom: 20px; border-radius: 8px; }
        label { display: block; margin-top: 10px; font-weight: bold; }
        input, textarea { width: 100%; padding: 8px; margin-top: 5px; box-sizing: border-box; }
        button { background-color: #007bff; color: white; padding: 10px 20px; border: none; border-radius: 4px; margin-top: 15px; cursor: pointer; }
        button:hover { background-color: #0056b3; }
        h2 { margin-top: 0; }
        .success { color: green; display: none; margin-top: 10px; }
        .error { color: red; display: none; margin-top: 10px; }
    </style>
</head>
<body>
    <h1>管理画面 (Admin)</h1>

    <!-- Discount Form -->
    <div class="card">
        <h2>割引商品の追加</h2>
        <form id="discountForm">
            <label>商品名 (Name):</label>
            <input type="text" name="name" required placeholder="例: サンドイッチ">
            
            <label>元値 (Old Price):</label>
            <input type="number" name="oldPrice" required placeholder="350">
            
            <label>割引価格 (New Price):</label>
            <input type="number" name="newPrice" required placeholder="240">
            
            <label>理由 (Reason):</label>
            <input type="text" name="reason" placeholder="例: 消費期限が近いため">
            
            <label>アイコン (Emoji/Icon):</label>
            <input type="text" name="icon" required placeholder="🥪">

            <button type="submit">商品を追加</button>
            <div class="success" id="discountSuccess">追加しました！</div>
            <div class="error" id="discountError">エラーが発生しました</div>
        </form>
    </div>

    <!-- Recipe Form -->
    <div class="card">
        <h2>レシピの追加</h2>
        <form id="recipeForm">
            <label>レシピ名 (Name):</label>
            <input type="text" name="name" required>
            
            <label>説明 (Description):</label>
            <textarea name="description" required></textarea>
            
            <label>分量 (Servings):</label>
            <input type="text" name="servings" required placeholder="例: 2人分">
            
            <label>材料 (Ingredients) - 1行に1つ:</label>
            <textarea name="ingredients" required placeholder="豚肉: 100g&#10;玉ねぎ: 1個"></textarea>
            
            <label>調味料 (Seasonings) - 1行に1つ:</label>
            <textarea name="seasonings" required placeholder="塩: 少々&#10;胡椒: 少々"></textarea>
            
            <label>作り方 (Instructions) - 1行に1つ:</label>
            <textarea name="instructions" required placeholder="材料を切ります&#10;炒めます"></textarea>
            
            <label>コツ・ポイント (Tips) - 1行に1つ:</label>
            <textarea name="tips" required placeholder="強火で炒めると美味しいです"></textarea>

            <button type="submit">レシピを追加</button>
            <div class="success" id="recipeSuccess">追加しました！</div>
            <div class="error" id="recipeError">エラーが発生しました</div>
        </form>
    </div>

    <script>
        // Discount Submit Handler
        document.getElementById('discountForm').addEventListener('submit', async (e) => {
            e.preventDefault();
            const formData = new FormData(e.target);
            const data = {
                name: formData.get('name'),
                oldPrice: parseInt(formData.get('oldPrice')),
                newPrice: parseInt(formData.get('newPrice')),
                reason: formData.get('reason') || null,
                icon: formData.get('icon')
            };

            sendData('/discounts', data, 'discountSuccess', 'discountError', e.target);
        });

        // Recipe Submit Handler
        document.getElementById('recipeForm').addEventListener('submit', async (e) => {
            e.preventDefault();
            const formData = new FormData(e.target);
            
            // Convert newline separated strings to arrays
            const ingredients = formData.get('ingredients').split('\n').filter(line => line.trim() !== '');
            const seasonings = formData.get('seasonings').split('\n').filter(line => line.trim() !== '');
            const tips = formData.get('tips').split('\n').filter(line => line.trim() !== '');
            
            // Simple instruction handling (text only for now)
            const instructionsText = formData.get('instructions').split('\n').filter(line => line.trim() !== '');
            const instructions = instructionsText.map(desc => ({ description: desc, imageUrl: null }));

            const data = {
                name: formData.get('name'),
                description: formData.get('description'),
                imageUrl: null,
                servings: formData.get('servings'),
                ingredients: ingredients,
                seasonings: seasonings,
                instructions: instructions,
                tips: tips
            };

            sendData('/recipes', data, 'recipeSuccess', 'recipeError', e.target);
        });

        async function sendData(url, data, successId, errorId, form) {
            const successEl = document.getElementById(successId);
            const errorEl = document.getElementById(errorId);
            successEl.style.display = 'none';
            errorEl.style.display = 'none';

            try {
                const response = await fetch(url, {
                    method: 'POST',
                    headers: { 'Content-Type': 'application/json' },
                    body: JSON.stringify(data)
                });

                if (response.ok) {
                    successEl.style.display = 'block';
                    form.reset();
                    setTimeout(() => successEl.style.display = 'none', 3000);
                } else {
                    errorEl.textContent = 'エラー: ' + response.status;
                    errorEl.style.display = 'block';
                }
            } catch (err) {
                console.error(err);
                errorEl.textContent = '通信エラー';
                errorEl.style.display = 'block';
            }
        }
    </script>
</body>
</html>
"""
