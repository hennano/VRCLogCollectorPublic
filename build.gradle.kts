plugins {
    kotlin("jvm") version "1.8.0"
    kotlin("plugin.serialization") version "1.6.10"
    id("org.jetbrains.dokka") version "1.8.10"
    application
}

group = "net.hennabatch"
version = "3.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.illposed.osc:javaosc-core:0.8") {
        exclude("org.slf4j", "slf4j-log4j12")
        exclude("log4j", "log4j")
    }
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
    implementation("org.slf4j:slf4j-api:2.0.7")
    implementation("org.slf4j:slf4j-ext:2.0.7")
    implementation("ch.qos.logback:logback-core:1.4.6")
    implementation("ch.qos.logback:logback-classic:1.4.6")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.0")

    //テスト用など
    testImplementation("org.jetbrains.kotlin:kotlin-test:1.8.20-RC")
    testImplementation("io.kotest:kotest-runner-junit5:5.5.5")
    testImplementation("io.kotest:kotest-assertions-json-jvm:5.5.5")
    testImplementation("io.mockk:mockk:1.13.4")
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(17)
}

application {
    mainClass.set("MainKt")
}