plugins {
    kotlin("jvm")
    id("io.ktor.plugin") version "2.3.7"
    id("com.github.johnrengelman.shadow") version "8.1.1"
    application
}

group = "com.kit.server"
version = "1.0-SNAPSHOT"

application {
    mainClass.set("com.kit.server.ApplicationKt")
}

dependencies {
    implementation("io.ktor:ktor-server-core-jvm:2.3.7")
    implementation("io.ktor:ktor-server-netty-jvm:2.3.7")
    implementation("io.ktor:ktor-server-content-negotiation-jvm:2.3.7")
    implementation("io.ktor:ktor-serialization-gson-jvm:2.3.7")
    implementation("ch.qos.logback:logback-classic:1.4.14")
    
    // Add authentication dependency
    implementation("io.ktor:ktor-server-auth-jvm:2.3.7") 
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions {
        jvmTarget = "17"
    }
}
