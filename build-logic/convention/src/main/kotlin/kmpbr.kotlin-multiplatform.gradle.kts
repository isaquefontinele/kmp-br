// Base configuration shared by every kmpbr-* library module.
// Targets beyond the JVM are added in a follow-up (ROADMAP Milestone 0, "Targets").

plugins {
    id("org.jetbrains.kotlin.multiplatform")
}

group = "io.github.isaquefontinele.kmpbr"
version = providers.gradleProperty("kmpbr.version").get()

kotlin {
    explicitApi()
    jvmToolchain(17)

    jvm()

    sourceSets {
        commonTest.dependencies {
            implementation(kotlin("test"))
        }
    }
}
