plugins {
    kotlin("jvm") version Config.Versions.Kotlin.kotlin
    kotlin("plugin.serialization") version Config.Versions.Kotlin.kotlin
    id("maven")
}

allprojects {
    tasks.withType(org.jetbrains.kotlin.gradle.tasks.KotlinCompile::class).all {
        JavaVersion.VERSION_1_8.toString().also {
            kotlinOptions.jvmTarget = it
            if (plugins.hasPlugin("org.jetbrains.kotlin.jvm")) {
                sourceCompatibility = it
                targetCompatibility = it
            }
        }
    }
}

kotlin {
    explicitApiWarning()
    sourceSets.all {
        languageSettings.useExperimentalAnnotation("kotlin.RequiresOptIn")
    }
}

repositories {
    mavenCentral()
    jcenter()
}

group = "com.petersamokhin.notionsdk"
version = "1.0.4"


dependencies {
    implementation(kotlin("stdlib-jdk8", Config.Versions.Kotlin.kotlin))

    implementation("org.jetbrains.kotlinx", "kotlinx-serialization-core", Config.Versions.Kotlin.serialization)
    implementation("org.jetbrains.kotlinx", "kotlinx-serialization-json", Config.Versions.Kotlin.serialization)

    implementation("io.ktor", "ktor-client-core", Config.Versions.ktor)
}