@file:OptIn(ExperimentalKotlinGradlePluginApi::class)

import org.jetbrains.compose.ExperimentalComposeLibrary
import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSetTree
import org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpackConfig
import java.util.Properties

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsCompose)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.kotlinSerialization)
    alias(libs.plugins.gradleBuildConfig)
}

kotlin {
    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {
        moduleName = "composeApp"
        browser {
            val rootDirPath = project.rootDir.path
            val projectDirPath = project.projectDir.path
            commonWebpackConfig {
                outputFileName = "composeApp.js"
                devServer = (devServer ?: KotlinWebpackConfig.DevServer()).apply {
                    static = (static ?: mutableListOf()).apply {
                        // Serve sources to debug inside browser
                        add(rootDirPath)
                        add(projectDirPath)
                    }
                }
            }
        }
        binaries.executable()
    }

    androidTarget {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
        instrumentedTestVariant.sourceSetTree.set(KotlinSourceSetTree.test)
    }

    jvm("desktop")

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
        }
    }

    applyDefaultHierarchyTemplate {
        common {
            group("commonAuth") {
                withIos()
                withAndroidTarget()
                withJvm()
            }
        }
    }

    sourceSets {
        val commonAuthMain by getting {
            dependencies {
                implementation(libs.kmpauth.google)
                implementation(libs.kmpauth.firebase)
                implementation(libs.kmpauth.uihelper)
            }
        }

        val desktopMain by getting {
            dependsOn(commonAuthMain)
        }

        androidMain {
            dependsOn(commonAuthMain)
            dependencies {
                implementation(compose.preview)
                implementation(libs.androidx.activity.compose)
                implementation(libs.koin.android)
                implementation(libs.ktor.client.okhttp)
            }
        }
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.materialIconsExtended)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(libs.androidx.lifecycle.viewmodel)
            implementation(libs.androidx.lifecycle.runtime.compose)
            implementation(libs.androidx.navigation.compose)
            implementation(libs.kotlinx.serialization.json)
            implementation(projects.shared)
            implementation(libs.koin.compose)
            implementation(libs.coil.compose)
            implementation(libs.coil.network.ktor3)
        }
        desktopMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(libs.kotlinx.coroutines.swing)
            implementation(libs.ktor.client.okhttp)
        }
        iosMain {
            dependsOn(commonAuthMain)
            dependencies {
                implementation(libs.ktor.client.ios)
            }
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
            @OptIn(ExperimentalComposeLibrary::class) implementation(compose.uiTest)
        }
    }
}

android {
    namespace = "io.devexpert.kmptraining"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "io.devexpert.kmptraining"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    sourceSets {
        getByName("debug") {
            manifest.srcFile("src/androidDebug/AndroidManifest.xml")
        }
    }
}

dependencies {
    debugImplementation(compose.uiTooling)
    androidTestImplementation(libs.androidx.compose.ui.test.junit)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
}

compose.desktop {
    application {
        mainClass = "io.devexpert.kmptraining.MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "io.devexpert.kmptraining"
            packageVersion = "1.0.0"
        }
    }
}

buildConfig {
    packageName("io.devexpert.kmptraining")

    val properties = Properties()
    properties.load(project.rootProject.file("local.properties").reader())

    buildConfigField("GOOGLE_SERVER_ID", properties.getProperty("GOOGLE_SERVER_ID"))
}