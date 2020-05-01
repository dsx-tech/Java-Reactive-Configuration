import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.3.41"
}

group = "uk.dsx.reactiveconfig"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    jcenter()
}

tasks {
    "test"(Test::class) {
        useJUnitPlatform()
    }
}

dependencies {
    val spekVersion = "2.0.8"
    val JUnitVersion = "5.6.2"

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.3.0")
    implementation(kotlin("stdlib-jdk8"))
    implementation("com.beust:klaxon:5.0.1")

    testImplementation("org.spekframework.spek2:spek-dsl-jvm:$spekVersion")
    testImplementation("org.junit.jupiter:junit-jupiter-api:$JUnitVersion")
    testImplementation(kotlin("test"))
    testRuntimeOnly("org.spekframework.spek2:spek-runner-junit5:$spekVersion")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:$JUnitVersion")
    testRuntimeOnly(kotlin("reflect"))

    compile("org.slf4j:slf4j-simple:1.7.26")
    compile("io.github.microutils:kotlin-logging:1.7.8")
    compile("org.yaml:snakeyaml:1.18")
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}