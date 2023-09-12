val ktor_version: String by project
val kotlin_version: String by project
val logback_version: String by project
val exposed_version: String by project
val postgresql_version: String by project
val koin_version: String by project

plugins {
    kotlin("jvm") version "1.9.10"
    id("io.ktor.plugin") version "2.3.4"
    id ("org.jetbrains.kotlin.plugin.serialization") version "1.8.0"
    id ("io.gitlab.arturbosch.detekt") version "1.18.1"

}
java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

group = "com.example"
version = "0.0.1"

application {
    mainClass.set("io.ktor.server.netty.EngineMain")

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.ktor:ktor-server-core-jvm")
    implementation("io.ktor:ktor-server-netty-jvm")
    implementation("ch.qos.logback:logback-classic:$logback_version")
    testImplementation("io.ktor:ktor-server-tests-jvm")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlin_version")
    //Content Negotiation
    implementation("io.ktor:ktor-server-content-negotiation:$ktor_version")
    //Json serialization
    implementation("io.ktor:ktor-serialization-kotlinx-json:$ktor_version")
    implementation("io.ktor:ktor-serialization-gson:$ktor_version")

    //Request validation
    implementation("io.ktor:ktor-server-request-validation:$ktor_version")
    //Status pages
    implementation("io.ktor:ktor-server-status-pages:$ktor_version")
    //Database Exposed
    implementation("org.jetbrains.exposed:exposed-core:$exposed_version")
    implementation("org.jetbrains.exposed:exposed-dao:$exposed_version")
    implementation("org.jetbrains.exposed:exposed-jdbc:$exposed_version")
    //Postgresql dependency
    implementation("org.postgresql:postgresql:$postgresql_version")
    //koin
    implementation("io.insert-koin:koin-ktor:$koin_version")
    //mockk
    testImplementation("io.mockk:mockk:1.13.3")
    //h2 database
    implementation("com.h2database:h2:1.4.192")
    // Add the H2 database dependency for testing
    testImplementation("com.h2database:h2:1.4.200")
    //call logging
    implementation("io.ktor:ktor-server-call-logging:$ktor_version")
    implementation("ch.qos.logback:logback-classic:$logback_version")
    testImplementation("org.testng:testng:7.1.0")



}
