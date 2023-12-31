import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    kotlin("jvm") version "1.9.0"
    kotlin("plugin.serialization") version "1.9.0"
    application
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

group = "org.cht"
version = "1.0.1"

repositories {
    mavenCentral()
}

dependencies {

    implementation(kotlin("stdlib-jdk8"))
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.1")
    implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.4.0")
    // Logging
    implementation("io.github.microutils:kotlin-logging-jvm:3.0.5")
    implementation("ch.qos.logback:logback-classic:1.4.8")

    implementation("com.influxdb:influxdb-client-kotlin:6.10.0")

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
    implementation("commons-io:commons-io:2.12.0")
    implementation("com.google.guava:guava:32.0.0-jre")


    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(11)
}

application {
    mainClass.set("org.cht.MainKt")
}

tasks.withType<ShadowJar>() {
    manifest {
        attributes["Main-Class"] = "org.cht.MainKt"
    }
    archiveFileName="hermes.jar"
}

tasks {
    build {
        dependsOn(shadowJar)
    }
}


sourceSets.main.configure {
    resources.srcDirs("src/main/resources").includes.addAll(arrayOf("**/*.*"))
}

tasks {
    withType<Copy> {
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    }
}
