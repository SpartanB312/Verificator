plugins {
    java
    kotlin("jvm") version "1.6.10"
}

group = "net.spartanb312"
version = "1.0"

repositories {
    mavenCentral()
}

val kotlinxCoroutineVersion = "1.5.2"
val library: Configuration by configurations.creating

dependencies {
    library("org.jetbrains.kotlinx:kotlinx-coroutines-core:$kotlinxCoroutineVersion")
    implementation(library)
}

tasks {
    compileJava {
        options.encoding = "UTF-8"
        sourceCompatibility = "1.8"
        targetCompatibility = "1.8"
    }

    compileKotlin {
        kotlinOptions {
            jvmTarget = "1.8"
            freeCompilerArgs = listOf("-Xopt-in=kotlin.RequiresOptIn", "-Xinline-classes")
        }
    }

    jar {
        manifest {
            attributes(
                "Main-Class" to "net.spartanb312.phantom.server.MainKt"
            )
        }

        duplicatesStrategy = org.gradle.api.file.DuplicatesStrategy.EXCLUDE

        from(
            library.map {
                if (it.isDirectory) it
                else zipTree(it)
            }
        )
    }
}
