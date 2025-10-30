package com.kit.server

import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.routing.*
import io.ktor.server.response.*
import io.ktor.server.request.*
import io.ktor.http.*
import io.ktor.server.plugins.cors.routing.*
import kotlinx.serialization.Serializable

// Exposed / Hikari / Flyway
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.flywaydb.core.Flyway
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.UUID
import kotlin.math.max

@Serializable
data class EntryReq(
    val name: String,
    val barcode: String? = null,
    val qty: Int = 0,
    val expires: String? = null

)
@kotlinx.serialization.Serializable
data class ApiResp(val ok: Boolean, val id: Long? = null, val error: String? = null)

// ====== 表定义 ======
object Entries : Table("entries") {
    val id = long("id").autoIncrement()
    val userId = uuid("user_id").nullable()
    val name = text("name")
    val barcode = text("barcode").nullable()
    val qty = integer("qty").default(0)
    val expires = date("expires").nullable()
    val createdAt = datetime("created_at")
    override val primaryKey = PrimaryKey(id)
}

// ====== DB 初始化（连接池 + 迁移）======
fun initDbByEnv(): Database {
    val url  = System.getenv("DB_URL") ?: "jdbc:postgresql://127.0.0.1:5432/appdb"
    val user = System.getenv("DB_USER") ?: "app"
    val pass = System.getenv("DB_PASS") ?: "app"

    // Flyway 迁移
    Flyway.configure().dataSource(url, user, pass).load().migrate()

    // HikariCP 连接池
    val cfg = HikariConfig().apply {
        jdbcUrl = url
        username = user
        password = pass
        maximumPoolSize = 8
        minimumIdle = 1
        isAutoCommit = false
        connectionTimeout = 10_000
        addDataSourceProperty("cachePrepStmts", "true")
        addDataSourceProperty("prepStmtCacheSize", "250")
        addDataSourceProperty("prepStmtCacheSqlLimit", "2048")
    }
    val ds = HikariDataSource(cfg)
    return Database.connect(ds)
}

fun main() {
    initDbByEnv()
    embeddedServer(Netty, port = 3031, host = "0.0.0.0") { // 必须 0.0.0.0
        install(ContentNegotiation) { json() }
        install(CORS) {
            anyHost()                           // 开发期先放开
            allowHeader(HttpHeaders.ContentType)
            allowMethod(HttpMethod.Post)
            allowMethod(HttpMethod.Get)
        }

        routing {
            get("/health") { call.respond(ApiResp(ok = true)) }

            post("/entries") {
                val req = call.receive<EntryReq>()
                require(req.name.isNotBlank()) { "name is required" }
                val newId = newSuspendedTransaction {
                    Entries.insertAndGetId {
                        it[userId]  = null                     // 等加入 JWT 再写入真实 uid
                        it[name]    = req.name.trim()
                        it[barcode] = req.barcode?.trim()
                        it[qty]     = max(0, req.qty)
                        it[expires] = req.expires?.let(LocalDate::parse)
                        it[createdAt] = LocalDateTime.now()
                    }.value
                }
                call.respond(ApiResp(ok = true, id = newId))
            }
        }

    }.start(wait = true)
}
