import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.3.31"
}

group = "com.petersamokhin"
version = "0.0.1"

repositories {
    mavenCentral()
    jcenter()
}

dependencies {
    val ktorVersion = "1.2.0"

    implementation(kotlin("stdlib-jdk8"))
    implementation("io.ktor:ktor-client-okhttp:$ktorVersion")
    implementation("io.ktor:ktor-client-json:$ktorVersion")
    implementation("io.ktor:ktor-client-gson:$ktorVersion")
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}