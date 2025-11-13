package com.kit.server

import io.ktor.server.application.*
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable

@Serializable
data class ApiResp(
    val ok: Boolean,
    val id: Long? = null,
    val error: String? = null
)

// 以后如果要用，可以保留；不用的话也没关系
@Serializable
data class EntryReq(
    val name: String,
    val barcode: String? = null,
    val qty: Int = 0,
    val expires: String? = null
)

@Serializable
data class DiscountItem(
    val id: Long,
    val name: String,
    val shop: String,
    val price: Int,
    val discountPrice: Int,
    val expires: String
)

fun main() {
    embeddedServer(
        Netty,
        port = 3031,
        host = "0.0.0.0"
    ) {
        install(ContentNegotiation) {
            json()
        }

        routing {
            // 健康检查 → JSON
            get("/health") {
                call.respond(ApiResp(ok = true))
            }

            // 割引商品一覧（暂时用假数据，以后再接数据库）
            get("/discount-items") {
                val items = listOf(
                    DiscountItem(
                        id = 1,
                        name = "牛乳 1L",
                        shop = "〇〇スーパー",
                        price = 198,
                        discountPrice = 98,
                        expires = "2025-11-15"
                    ),
                    DiscountItem(
                        id = 2,
                        name = "食パン 6枚切り",
                        shop = "△△ストア",
                        price = 158,
                        discountPrice = 80,
                        expires = "2025-11-14"
                    )
                )
                call.respond(items)
            }

            // 可选：简单的 /entries（现在只是假响应，不存数据库）
            post("/entries") {
                val req = call.receive<EntryReq>()
                // 这里先不做任何复杂处理，只是返回 ok + 虚拟 id
                val fakeId = 1L
                call.respond(ApiResp(ok = true, id = fakeId))
            }
        }
    }.start(wait = true)
}
