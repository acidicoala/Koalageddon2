import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
    id("org.jetbrains.compose")

    // https://github.com/gmazzo/gradle-buildconfig-plugin
    id("com.github.gmazzo.buildconfig") version "3.1.0"
}

group = "acidicoala"
version = "2.0-alpha01"

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
                jvmTarget = "18"
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
                implementation("org.kodein.di:kodein-di-framework-compose:7.16.0")

                // https://github.com/harawata/appdirs
                implementation("net.harawata:appdirs:1.2.1")

                // https://github.com/qos-ch/slf4j
                implementation("org.slf4j:slf4j-api:2.0.6")

                // https://github.com/tinylog-org/tinylog
                val tinylogVersion = "2.5.0"
                implementation("org.tinylog:tinylog-impl:$tinylogVersion")
                implementation("org.tinylog:slf4j-tinylog:$tinylogVersion")
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
            // TODO: Reference them from previous settings
            packageName = "koalageddon"
            packageVersion = "1.0.0"

            targetFormats(TargetFormat.Msi)

            // run `gradle suggestRuntimeModules` to get this list
            modules("java.instrument", "java.management", "java.naming", "java.sql", "jdk.unsupported")
        }
    }
}
