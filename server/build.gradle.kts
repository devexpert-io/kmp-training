import java.util.Properties

plugins {
    alias(libs.plugins.kotlinJvm)
    alias(libs.plugins.ktor)
    alias(libs.plugins.kotlinSerialization)
    application
    alias(libs.plugins.gradleBuildConfig)
}

group = "io.devexpert.kmptraining"
version = "1.0.0"
application {
    mainClass.set("io.devexpert.kmptraining.ApplicationKt")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=${extra["io.ktor.development"] ?: "false"}")
}

dependencies {
    implementation(projects.shared)
    implementation(libs.logback)
    implementation(libs.ktor.server.core)
    implementation(libs.ktor.server.netty)
    implementation(libs.ktor.server.contentNegotiation)
    implementation(libs.ktor.serialization.kotlin.json)
    implementation(libs.ktor.server.cors)
    implementation(libs.ktor.server.auth)
    implementation(libs.ktor.server.auth.jwt)
    implementation(libs.ktor.client.core)
    implementation(libs.ktor.client.okhttp)
    implementation(libs.ktor.client.contentNegotiation)

    implementation(libs.exposed.core)
    implementation(libs.exposed.dao)
    implementation(libs.exposed.jdbc)
    implementation(libs.h2)
    implementation(libs.hikaricp)

    implementation(libs.google.api.client)
    testImplementation(libs.kotlin.test.junit)
}

buildConfig {
    packageName("io.devexpert.kmptraining")

    val properties = Properties()
    properties.load(project.rootProject.file("local.properties").reader())

    buildConfigField("JWT_SECRET", properties.getProperty("JWT_SECRET"))
    buildConfigField("GOOGLE_SERVER_ID", properties.getProperty("GOOGLE_SERVER_ID"))
}