import org.jetbrains.compose.ExperimentalComposeLibrary
import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    kotlin("multiplatform")
    id("org.jetbrains.compose")
    kotlin("plugin.serialization")
}

group = "acidicoala"
version = "2.0-alpha01"

repositories {
    google()
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
}

kotlin {
    jvm {
        compilations.all {
            kotlinOptions {
                jvmTarget = "11"
                freeCompilerArgs += "-opt-in=kotlin.RequiresOptIn"
            }
        }
        withJava()
    }
    sourceSets {
        val jvmMain by getting {
            @OptIn(ExperimentalComposeLibrary::class)
            dependencies {
                implementation(compose.desktop.currentOs)
                implementation(compose.materialIconsExtended)
                implementation(compose.material3)

                // https://github.com/Kotlin/kotlinx.serialization
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.4.1")

                // https://github.com/kosi-libs/Kodein
                implementation("org.kodein.di:kodein-di-framework-compose:7.16.0")

                // https://github.com/harawata/appdirs
                implementation("net.harawata:appdirs:1.2.1")

                // https://github.com/MicroUtils/kotlin-logging
                implementation("io.github.microutils:kotlin-logging-jvm:3.0.4")

                // https://github.com/apache/logging-log4j2
                implementation("org.apache.logging.log4j:log4j-core:2.19.0")
            }
        }
        val jvmTest by getting
    }
}

compose.desktop {
    application {
        mainClass = "MainKt"
        nativeDistributions {
            targetFormats(TargetFormat.Msi)
            packageName = "koalageddon"
            packageVersion = "1.0.0"
        }
    }
}
