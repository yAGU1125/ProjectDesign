package com.kit.server

import io.ktor.http.*
import io.ktor.serialization.gson.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.UUID

// --- Data Classes (with default values to prevent crashes) ---
data class DiscountItem(
    val id: String = UUID.randomUUID().toString(),
    val name: String = "",
    val oldPrice: Int = 0,
    val newPrice: Int = 0,
    val reason: String? = null,
    val icon: String = ""
)

data class InstructionStep(
    val description: String = "",
    val imageUrl: String? = null
)

data class Recipe(
    val id: String = UUID.randomUUID().toString(),
    val name: String = "",
    val description: String = "",
    val imageUrl: String? = null,
    val servings: String = "",
    val ingredients: List<String> = emptyList(),
    val seasonings: List<String> = emptyList(),
    val instructions: List<InstructionStep> = emptyList(),
    val tips: List<String> = emptyList()
)

data class Notification(
    val id: String = UUID.randomUUID().toString(),
    val title: String = "",
    val content: String = "",
    val date: String = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm"))
)

// --- In-memory Storage (Full Data Restored) ---
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

val notifications = mutableListOf(
    Notification(
        title = "新しいレシピを追加しました！",
        content = "期間限定のプレミアムチョコクロワッサンのレシピを公開しました。ぜひチェックしてみてください！"
    ),
    Notification(
        title = "メンテナンスのお知らせ",
        content = "本日23:00より、サーバーメンテナンスを実施します。",
        date = "2024/05/20 18:00"
    ),
    Notification(
        title = "アプリバージョンアップのお知らせ",
        content = "新しい機能を追加したバージョン2.0をリリースしました。最新の機能をお楽しみいただくために、ストアからアップデートしてください。",
        date = "2024/05/18 12:00"
    ),
    Notification(
        title = "割引クーポンプレゼント！",
        content = "いつもご利用ありがとうございます。本日限定で利用できる10%OFFクーポンをプレゼントします！マイページからご確認ください。",
        date = "2024/05/15 09:00"
    ),
    Notification(
        title = "ようこそ！",
        content = "ProjectDesignへようこそ！あなたの毎日が、もっと豊かになりますように。",
        date = "2024/05/10 15:00"
    )
)

fun main() {
    embeddedServer(Netty, port = 3031, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    install(Authentication) {
        basic("admin-auth") {
            realm = "Ktor Server"
            validate { credentials ->
                if (credentials.name == "admin" && credentials.password == "projectdesign") {
                    UserIdPrincipal(credentials.name)
                } else null
            }
        }
    }

    install(ContentNegotiation) {
        gson { setPrettyPrinting() }
    }

    routing {
        // --- Public API Routes ---
        get("/discounts") { call.respond(discountItems) }
        get("/recipes") { call.respond(recipes) }
        get("/notifications") { call.respond(notifications) }

        // --- Pages ---
        get("/login") { call.respondText(loginHtml, ContentType.Text.Html) }

        // IMPORTANT: /admin 页面本身不做 Basic Auth（否则浏览器跳转永远 401）
        // 真正的“写操作”与“校验”放在需要认证的 API 上
        get("/admin") { call.respondText(adminHtml, ContentType.Text.Html) }

        // --- Admin Routes (Protected) ---
        authenticate("admin-auth") {
            // login 用来验证密码是否正确
            get("/admin/ping") { call.respondText("ok") }

            post("/discounts") {
                val item = call.receive<DiscountItem>().copy(id = UUID.randomUUID().toString())
                discountItems.add(0, item)
                call.respond(HttpStatusCode.Created, item)
            }

            post("/recipes") {
                val item = call.receive<Recipe>().copy(id = UUID.randomUUID().toString())
                recipes.add(0, item)
                call.respond(HttpStatusCode.Created, item)
            }

            post("/notifications") {
                val item = call.receive<Notification>().copy(
                    id = UUID.randomUUID().toString(),
                    date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm"))
                )
                notifications.add(0, item)
                call.respond(HttpStatusCode.Created, item)
            }

            delete("/delete/{type}/{id}") {
                val type = call.parameters["type"]
                val id = call.parameters["id"]
                val removed = when (type) {
                    "notification" -> notifications.removeIf { it.id == id }
                    "discount" -> discountItems.removeIf { it.id == id }
                    "recipe" -> recipes.removeIf { it.id == id }
                    else -> false
                }
                if (removed) call.respond(HttpStatusCode.OK) else call.respond(HttpStatusCode.NotFound)
            }
        }
    }
}

// --- HTML Templates ---

val loginHtml = """
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8" />
    <title>Admin Login</title>
    <style>
        body { font-family: sans-serif; max-width: 520px; margin: 50px auto; padding: 20px; }
        label, input, button { display: block; width: 100%; margin-top: 10px; box-sizing: border-box; }
        input, button { padding: 10px; }
        .hint { color: #666; font-size: 12px; margin-top: 12px; }
        .error { color: #c00; margin-top: 12px; }
    </style>
</head>
<body>
    <h2>Admin Login</h2>

    <label>User</label>
    <input type="text" id="user" value="admin" readonly />

    <label>Password</label>
    <input type="password" id="password" placeholder="projectdesign" />

    <button type="button" onclick="login()">Login</button>
    <div class="hint">※ パスワードが正しければ /admin に移動します。</div>
    <div id="error" class="error"></div>

    <script>
        async function login() {
            const user = document.getElementById('user').value;
            const password = document.getElementById('password').value;
            const errorEl = document.getElementById('error');
            errorEl.textContent = '';

            const token = btoa(user + ':' + password);

            try {
                // 认证验证：成功才保存 token 并跳转
                const res = await fetch('/admin/ping', {
                    headers: { 'Authorization': 'Basic ' + token }
                });

                if (!res.ok) {
                    errorEl.textContent = 'Login failed. パスワードが違います。';
                    return;
                }

                sessionStorage.setItem('adminToken', token);
                window.location.href = '/admin';
            } catch (e) {
                console.error(e);
                errorEl.textContent = 'Network error.';
            }
        }
    </script>
</body>
</html>
""".trimIndent()

val adminHtml = """
<!DOCTYPE html>
<html lang="ja">
<head>
    <meta charset="UTF-8" />
    <title>Admin</title>
    <style>
        body { font-family: sans-serif; max-width: 900px; margin: auto; padding: 20px; }
        .card { border: 1px solid #ccc; padding: 20px; margin-bottom: 20px; border-radius: 8px; }
        .item { border-bottom: 1px solid #eee; padding: 10px; display: flex; justify-content: space-between; align-items: center; gap: 12px; }
        .delete-btn { color: red; cursor: pointer; white-space: nowrap; }
        label, input, textarea, button { display: block; width: 100%; margin-top: 10px; box-sizing: border-box; padding: 8px; }
        textarea { min-height: 80px; }
        button { margin-top: 20px; cursor: pointer; }
        .row { display: grid; grid-template-columns: 1fr 1fr; gap: 12px; }
        @media (max-width: 720px) { .row { grid-template-columns: 1fr; } }
    </style>
</head>
<body>
    <h1>管理画面</h1>

    <div class="card">
        <h2>お知らせ一覧</h2>
        <div id="notificationsList"></div>
    </div>

    <div class="card">
        <h2>割引商品一覧</h2>
        <div id="discountsList"></div>
    </div>

    <div class="card">
        <h2>レシピ一覧</h2>
        <div id="recipesList"></div>
    </div>

    <div class="card">
        <h2>お知らせの追加</h2>
        <form id="notificationForm">
            <label>タイトル:</label><input type="text" name="title" required />
            <label>内容:</label><textarea name="content" required></textarea>
            <button type="submit">お知らせを追加</button>
        </form>
    </div>

    <div class="card">
        <h2>割引商品の追加</h2>
        <form id="discountForm">
            <label>商品名:</label><input type="text" name="name" required />
            <div class="row">
                <div>
                    <label>元値:</label><input type="number" name="oldPrice" required />
                </div>
                <div>
                    <label>割引価格:</label><input type="number" name="newPrice" required />
                </div>
            </div>
            <label>理由:</label><input type="text" name="reason" />
            <label>アイコン:</label><input type="text" name="icon" required />
            <button type="submit">商品を追加</button>
        </form>
    </div>

    <div class="card">
        <h2>レシピの追加</h2>
        <form id="recipeForm">
            <label>レシピ名:</label><input type="text" name="name" required />
            <label>説明:</label><textarea name="description" required></textarea>
            <label>分量:</label><input type="text" name="servings" required />
            <label>材料 (1行1つ):</label><textarea name="ingredients" required></textarea>
            <label>調味料 (1行1つ):</label><textarea name="seasonings" required></textarea>
            <label>作り方 (1行1つ):</label><textarea name="instructions" required></textarea>
            <label>コツ (1行1つ):</label><textarea name="tips" required></textarea>
            <button type="submit">レシピを追加</button>
        </form>
    </div>

<script>
const token = sessionStorage.getItem('adminToken');
if (!token) {
    window.location.href = '/login';
}
const authHeader = { 'Authorization': 'Basic ' + token };

async function loadData() {
    try {
        const notifications = await (await fetch('/notifications')).json();
        document.getElementById('notificationsList').innerHTML =
            notifications.map(n =>
                `<div class="item">
                    <span><b>${'$'}{n.title}</b>: ${'$'}{n.content}</span>
                    <span class="delete-btn" onclick="deleteItem('notification','${'$'}{n.id}')">削除</span>
                 </div>`
            ).join('');

        const discounts = await (await fetch('/discounts')).json();
        document.getElementById('discountsList').innerHTML =
            discounts.map(d =>
                `<div class="item">
                    <span>${'$'}{d.icon} <b>${'$'}{d.name}</b> (${ '$' }{d.newPrice}円)</span>
                    <span class="delete-btn" onclick="deleteItem('discount','${'$'}{d.id}')">削除</span>
                 </div>`
            ).join('');

        const recipes = await (await fetch('/recipes')).json();
        document.getElementById('recipesList').innerHTML =
            recipes.map(r =>
                `<div class="item">
                    <span><b>${'$'}{r.name}</b>: ${'$'}{r.description}</span>
                    <span class="delete-btn" onclick="deleteItem('recipe','${'$'}{r.id}')">削除</span>
                 </div>`
            ).join('');
    } catch (e) {
        console.error('Failed to load data:', e);
        sessionStorage.removeItem('adminToken');
        window.location.href = '/login';
    }
}

async function deleteItem(type, id) {
    if (!confirm('本当に削除しますか？')) return;

    const res = await fetch(`/delete/${'$'}{type}/${'$'}{id}`, {
        method: 'DELETE',
        headers: authHeader
    });

    if (res.ok) loadData();
    else alert('削除に失敗しました。');
}

document.getElementById('notificationForm').addEventListener('submit', async (e) => {
    e.preventDefault();
    const data = { title: e.target.title.value, content: e.target.content.value };

    const res = await fetch('/notifications', {
        method: 'POST',
        headers: { ...authHeader, 'Content-Type': 'application/json' },
        body: JSON.stringify(data)
    });

    if (!res.ok) { alert('追加に失敗しました'); return; }
    alert('お知らせを追加しました');
    e.target.reset();
    loadData();
});

document.getElementById('discountForm').addEventListener('submit', async (e) => {
    e.preventDefault();
    const data = {
        name: e.target.name.value,
        oldPrice: parseInt(e.target.oldPrice.value, 10),
        newPrice: parseInt(e.target.newPrice.value, 10),
        reason: (e.target.reason.value || '').trim() === '' ? null : e.target.reason.value.trim(),
        icon: e.target.icon.value
    };

    const res = await fetch('/discounts', {
        method: 'POST',
        headers: { ...authHeader, 'Content-Type': 'application/json' },
        body: JSON.stringify(data)
    });

    if (!res.ok) { alert('追加に失敗しました'); return; }
    alert('割引商品を追加しました');
    e.target.reset();
    loadData();
});

document.getElementById('recipeForm').addEventListener('submit', async (e) => {
    e.preventDefault();
    const toList = (str) => str.split('\n').map(s => s.trim()).filter(Boolean);

    const data = {
        name: e.target.name.value,
        description: e.target.description.value,
        servings: e.target.servings.value,
        ingredients: toList(e.target.ingredients.value),
        seasonings: toList(e.target.seasonings.value),
        instructions: toList(e.target.instructions.value).map(d => ({ description: d, imageUrl: null })),
        tips: toList(e.target.tips.value)
    };

    const res = await fetch('/recipes', {
        method: 'POST',
        headers: { ...authHeader, 'Content-Type': 'application/json' },
        body: JSON.stringify(data)
    });

    if (!res.ok) { alert('追加に失敗しました'); return; }
    alert('レシピを追加しました');
    e.target.reset();
    loadData();
});

loadData();
</script>
</body>
</html>
""".trimIndent()
