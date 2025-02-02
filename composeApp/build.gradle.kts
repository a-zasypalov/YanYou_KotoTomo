import org.gradle.kotlin.dsl.android
import org.gradle.kotlin.dsl.compose
import org.gradle.kotlin.dsl.dependencies

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.sqldelight)
    alias(libs.plugins.kotlin.serialization)
}

sqldelight {
    database("YanYouKotoTomoDatabase") {
        packageName = "com.gaoyun.yanyou_kototomo.data.persistence"
        sourceFolders = listOf("kotlin")
        dialect = "sqlite:3.24"
        version = 1
    }
    linkSqlite = true
}

kotlin {
    androidTarget()

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "YanYouKotoTomoComposeApp"
            isStatic = true
        }
    }

    targets.all {
        compilations.all {
            compilerOptions.configure {
                freeCompilerArgs.add("-Xexpect-actual-classes")
            }
        }
    }

    jvmToolchain(17)

    sourceSets {
        androidMain.dependencies {
            implementation(compose.preview)
            implementation(libs.androidx.activity.compose)
            implementation(libs.koin.android)
            implementation(libs.koin.androidx)
            implementation(libs.sqldelight.androidDriver)
            implementation(libs.androidx.material)
        }
        iosMain.dependencies {
            implementation(libs.ktor.client.ios)
            implementation(libs.sqldelight.nativeDriver)
        }
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.ui)
            implementation(compose.animation)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(compose.materialIconsExtended)
            implementation(libs.navigation)
            implementation(libs.androidx.lifecycle.viewmodel)
            implementation(libs.androidx.lifecycle.runtime.compose)

            implementation(libs.kotlin.serialization.core)
            implementation(libs.kotlin.datetime)
            implementation(libs.koin.core)
            implementation(libs.koin.compose)
            implementation(libs.koin.compose.viewmodel)
            implementation(libs.koin.compose.viewmodel.navigation)
            implementation(libs.sqldelight.runtime)

            implementation(libs.ktor.client.core)
            implementation(libs.ktor.serialization)
            implementation(libs.ktor.serialization.json)
            implementation(libs.ktor.logging)
            implementation(libs.ktor.cio)
            implementation(libs.ktor.contentnegotiation)
            implementation(libs.ktor.contentencoding)

//            implementation(libs.precompose)
//            implementation(libs.precompose.viewmodel)
//            implementation(libs.precompose.koin)

            implementation(libs.compottie)
            implementation(libs.compottie.dot)
        }
    }
}

android {
    namespace = "com.gaoyun.yanyou_kototomo"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "com.gaoyun.yanyou_kototomo"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 1
        versionName = "0.0.1"
    }
    buildFeatures {
        buildConfig = true
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

dependencies {
    debugImplementation(compose.uiTooling)
}

