import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
    id("org.jetbrains.compose")
    id("com.github.gmazzo.buildconfig") version "3.1.0" // https://github.com/gmazzo/gradle-buildconfig-plugin
}

val author = "acidicoala"
val projectName = "Koalageddon"
val appVersion = "2.0.1"

group = author
version = appVersion

repositories {
    google()
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
}

buildConfig {
    buildConfigField("String", "APP_AUTHOR", """"${project.group}"""")
    buildConfigField("String", "APP_NAME", """"${project.name}"""")
    buildConfigField("String", "APP_VERSION", """"${project.version}"""")
    buildConfigField("long", "BUILD_TIME", "${System.currentTimeMillis()}L")
}

kotlin {
    jvm {
        compilations.all {
            kotlinOptions {
                jvmTarget = "11"
            }
        }
        withJava()
    }
    sourceSets {
        @Suppress("UNUSED_VARIABLE", "OPT_IN_IS_NOT_ENABLED")
        val jvmMain by getting {
            dependencies {
                // https://github.com/JetBrains/compose-jb
                implementation(compose.desktop.currentOs)
                implementation(compose.materialIconsExtended)

                // https://github.com/Kotlin/kotlinx.serialization
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.4.1")

                // https://github.com/kosi-libs/Kodein
                implementation("org.kodein.di:kodein-di-framework-compose:7.18.0")

                // https://github.com/harawata/appdirs
                implementation("net.harawata:appdirs:1.2.1")

                // https://github.com/qos-ch/slf4j
                implementation("org.slf4j:slf4j-api:2.0.6")

                // https://github.com/tinylog-org/tinylog
                val tinylogVersion = "2.6.0"
                implementation("org.tinylog:tinylog-impl:$tinylogVersion")
                implementation("org.tinylog:slf4j-tinylog:$tinylogVersion")

                // https://github.com/dorkbox/PeParser
                implementation("com.dorkbox:PeParser:3.1")

                // https://github.com/ktorio/ktor
                val ktorVersion = "2.2.3"
                implementation("io.ktor:ktor-client-core:$ktorVersion")
                implementation("io.ktor:ktor-client-cio:$ktorVersion")
                implementation("io.ktor:ktor-client-content-negotiation:$ktorVersion")
                implementation("io.ktor:ktor-serialization-kotlinx-json:$ktorVersion")
            }
        }

        @Suppress("UNUSED_VARIABLE")
        val jvmTest by getting
    }
}

compose.desktop {
    application {
        mainClass = "acidicoala.koalageddon.MainKt"
        nativeDistributions {
            packageName = projectName
            packageVersion = appVersion
            version = appVersion
            description = "A multi-store DLC Unlocker"
            copyright = "Fuck the copyright"
            vendor = author
            licenseFile.set(project.file("UNLICENSE.txt"))

            windows {
                iconFile.set(project.file("icon.ico"))
                menuGroup = projectName
                upgradeUuid = "B04EF055-B8A7-423E-8E6F-3835AE943C4E"
                shortcut = true
            }

            targetFormats(TargetFormat.Msi)

            // run `gradle suggestRuntimeModules` to get this list
            modules("java.instrument", "java.management", "java.naming", "java.sql", "jdk.unsupported")
        }

        buildTypes.release.proguard {
            configurationFiles.from("rules.pro")
        }
    }
}
