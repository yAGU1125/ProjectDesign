plugins {
    kotlin("jvm") version "2.0.0"
    kotlin("plugin.serialization") version "2.0.0"
    application
    id("com.github.johnrengelman.shadow") version "8.1.1" // ← 新增
}

application { mainClass.set("com.kit.server.AppKt") }

tasks {
    shadowJar {
        archiveBaseName.set("server-api")
        archiveVersion.set("")   // 生成 server-api.jar
        mergeServiceFiles()
    }
}


repositories { mavenCentral() }

val ktorVersion = "3.0.0"
dependencies {
    implementation("io.ktor:ktor-server-netty:$ktorVersion")
    implementation("io.ktor:ktor-server-content-negotiation:$ktorVersion")
    implementation("io.ktor:ktor-serialization-kotlinx-json:$ktorVersion")
    implementation("io.ktor:ktor-server-cors:$ktorVersion")
    implementation("ch.qos.logback:logback-classic:1.5.6") // 日志
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.7.3")
    implementation("org.jetbrains.exposed:exposed-core:0.54.0")
    implementation("org.jetbrains.exposed:exposed-dao:0.54.0")
    implementation("org.jetbrains.exposed:exposed-jdbc:0.54.0")
    implementation("com.zaxxer:HikariCP:5.1.0")                  // 连接池
    implementation("org.postgresql:postgresql:42.7.4")           // 驱动
    implementation("org.flywaydb:flyway-core:10.17.0")           // 数据库迁移
}

application {
    mainClass.set("com.kit.server.AppKt")
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
    kotlinOptions.jvmTarget = "21"
}
