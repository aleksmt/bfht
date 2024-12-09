import org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL

plugins {
    kotlin("jvm") version "2.1.0"
    id("io.gatling.gradle") version "3.13.1"
    id("org.jetbrains.kotlin.plugin.serialization") version "2.1.0"
}

group = "restapi.tests"
version = "1.0"

repositories {
    mavenCentral()
}

kotlin {
    jvmToolchain(17)
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

dependencies {
    // Standard deps for testing + kotlin essentials
    testImplementation(kotlin("stdlib"))
    testImplementation(kotlin("test"))

    // Async rest client
    testImplementation("io.ktor:ktor-client-core:3.0.1")
    testImplementation("io.ktor:ktor-client-cio:3.0.1")
    testImplementation("io.ktor:ktor-client-content-negotiation:3.0.1")
    testImplementation("io.ktor:ktor-client-websockets:3.0.1")
    testImplementation("io.ktor:ktor-serialization-kotlinx-json:3.0.1")

    // Performance testing
    testImplementation("io.gatling.highcharts:gatling-charts-highcharts:3.13.1")
    testImplementation("io.gatling:gatling-core:3.13.1")
    testImplementation("io.gatling:gatling-http:3.13.1")

    // Logging deps
    testImplementation("org.slf4j:slf4j-api:2.0.7")
    testImplementation("ch.qos.logback:logback-classic:1.5.12")

    // Suspend-methods support via runTest and runBlocking
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.3")

    // Configuration manager
    testImplementation("io.github.config4k:config4k:0.7.0")
}

tasks.test {
    useJUnitPlatform()
    maxParallelForks = 1

    testLogging {
        events("PASSED", "FAILED", "SKIPPED")
        showStandardStreams = true
        exceptionFormat = FULL
    }
}

gatling {
    includeMainOutput = true
    includeTestOutput = true
}

tasks.register<Exec>("startDockerContainer") {
    group = "Docker"
    description = "Start the Docker container for the application under test"
    commandLine("sh", "-c", " docker run -d --rm --name bfht -e VERBOSE=1 -p 8080:4242 todo-app:latest")
}

tasks.register<Exec>("restartDockerContainer") {
    group = "Docker"
    description = "Restart the Docker container for the application under test"
    commandLine("sh", "-c", """
        docker stop bfht || true &&
        docker run -d --rm --name bfht -e VERBOSE=1 -p 8080:4242 todo-app:latest
    """.trimIndent())
}

