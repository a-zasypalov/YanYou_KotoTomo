import org.gradle.kotlin.dsl.dependencies

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.sqldelight)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.kotest)
    jacoco
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
        androidUnitTest.dependencies {
            implementation(libs.mockk)
            implementation(libs.kotest)
            implementation(libs.kotest.assertions)
            implementation(libs.kotest.property)
            implementation(libs.kotest.runner)
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

            implementation(libs.compottie)
            implementation(libs.compottie.dot)

            implementation(libs.reorderable)
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

jacoco {
    toolVersion = "0.8.12" // Latest version
}

// Task to generate test coverage reports
tasks.register<JacocoReport>("jacocoTestReport") {
    dependsOn("testDebugUnitTest") // Ensure tests run before generating coverage

    reports {
        xml.required.set(true)
        html.required.set(true)
    }

    val fileFilter = listOf(
        "**/R.class", "**/R\$*.class",
        "**/BuildConfig.*", "**/Manifest*.*",
        "**/*Test*.*", // Exclude test classes
        "**/di/**",    // Exclude dependency injection (optional)
        "**/composedb/**" // Exclude SQLDelight DB classes (optional)
    )

    val javaClasses = fileTree("${buildDir}/intermediates/javac/debug/classes") {
        exclude(fileFilter)
    }

    val kotlinClasses = fileTree("${buildDir}/tmp/kotlin-classes/debug") {
        exclude(fileFilter)
    }

    sourceDirectories.setFrom(files("${project.projectDir}/src/commonMain/kotlin"))
    classDirectories.setFrom(files(javaClasses, kotlinClasses))
    executionData.setFrom(files("${buildDir}/jacoco/testDebugUnitTest.exec"))
}

// Run tests and generate report
tasks.withType<Test>().configureEach {
    useJUnitPlatform() // Kotest runs on JUnit 5
    finalizedBy("jacocoTestReport") // Generate report after tests
}