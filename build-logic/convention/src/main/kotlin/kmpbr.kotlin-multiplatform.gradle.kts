// Base configuration shared by every kmpbr-* library module.

import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl

plugins {
    id("org.jetbrains.kotlin.multiplatform")
    id("com.android.kotlin.multiplatform.library")
}

group = "io.github.isaquefontinele.kmpbr"
version = providers.gradleProperty("kmpbr.version").get()

val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")

useSettingsRepositoriesForJsTooling()

kotlin {
    explicitApi()
    jvmToolchain(17)

    jvm()

    android {
        // kmpbr-documents -> br.kmp.documents
        namespace = "br.kmp." + project.name.removePrefix("kmpbr-").replace("-", ".")
        compileSdk = libs.findVersion("android-compileSdk").get().requiredVersion.toInt()
        minSdk = libs.findVersion("android-minSdk").get().requiredVersion.toInt()
        withHostTest {}
    }

    iosArm64()
    iosSimulatorArm64()
    macosArm64()

    js {
        nodejs()
    }

    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {
        nodejs()
    }

    sourceSets {
        commonTest.dependencies {
            implementation(kotlin("test"))
        }
    }
}
