import net.minecraftforge.gradle.userdev.UserDevExtension
import org.spongepowered.asm.gradle.plugins.MixinExtension

val modGroup: String by extra
val modVersion: String by extra

group = modGroup
version = modVersion

buildscript {
    repositories {
        jcenter()
        maven("https://files.minecraftforge.net/maven")
        maven("https://repo.spongepowered.org/repository/maven-public/")
    }

    dependencies {
        classpath("net.minecraftforge.gradle:ForgeGradle:5.+")
        classpath("org.spongepowered:mixingradle:0.7-SNAPSHOT")
    }
}

plugins {
    java
    kotlin("jvm") version "1.6.10"
}

apply {
    plugin("net.minecraftforge.gradle")
    plugin("org.spongepowered.mixin")
}

repositories {
    jcenter()
    mavenCentral()
    maven("https://repo.spongepowered.org/repository/maven-public/")
    maven("https://jitpack.io")
}

val library: Configuration by configurations.creating

val kotlinVersion: String by project
val kotlinxCoroutineVersion: String by project

dependencies {

    library("org.jetbrains.kotlin:kotlin-stdlib:1.6.0")
    library(kotlin("reflect", kotlinVersion))
    library(kotlin("stdlib-jdk8", kotlinVersion))
    library("org.jetbrains.kotlinx:kotlinx-coroutines-core:$kotlinxCoroutineVersion")

    fun minecraft(dependencyNotation: Any): Dependency? = "minecraft"(dependencyNotation)

    fun jarOnly(dependencyNotation: Any) {
        library(dependencyNotation)
    }

    fun ModuleDependency.exclude(moduleName: String) =
        exclude(mapOf("module" to moduleName))


    minecraft("net.minecraftforge:forge:1.12.2-14.23.5.2860")

    library("org.spongepowered:mixin:0.8-SNAPSHOT") {
        exclude("commons-io")
        exclude("gson")
        exclude("guava")
        exclude("launchwrapper")
        exclude("log4j-core")
    }

    library(fileTree("lib"))

    annotationProcessor("org.spongepowered:mixin:0.8.3:processor") {
        exclude("gson")
    }

    implementation(library)

}

configure<MixinExtension> {
    add(sourceSets["main"], "mixins.phantom.refmap.json")
}

configure<UserDevExtension> {
    mappings(
        mapOf(
            "channel" to "stable",
            "version" to "39-1.12"
        )
    )

    runs {
        create("client") {
            workingDirectory = project.file("run").path

            properties(
                mapOf(
                    "forge.logging.markers" to "SCAN,REGISTRIES,REGISTRYDUMP",
                    "forge.logging.console.level" to "info",
                    "fml.coreMods.load" to "net.spartanb312.phantom.launch.DevFMLCoreMod",
                    "mixin.env.disableRefMap" to "true"
                )
            )
        }
    }
}


val client = task("client", type = Jar::class) {
    archiveClassifier.set("Client")

    from(
        library.map {
            if (it.isDirectory) it
            else zipTree(it)
        }
    )

    include(
        "net/spartanb312/phantom/**",
        "mixins.phantom.json",
        "mixins.phantom.refmap.json",
    )

    exclude(
        "net.spartanb312.phantom.launch.DevFMLCoreMod"
    )

    with(tasks["jar"] as CopySpec)
}

val loader = task("loader", type = Jar::class) {
    archiveClassifier.set("Loader")

    manifest {
        attributes(
            "FMLCorePluginContainsFMLMod" to "true",
            "FMLCorePlugin" to "phantomloader.FMLCoreMod",
            "TweakClass" to "org.spongepowered.asm.launch.MixinTweaker",
            "TweakOrder" to 0,
            "ForceLoadAsMod" to "true"
        )
    }

    from(
        library.map {
            if (it.isDirectory) it
            else zipTree(it)
        }
    )

    exclude(
        "mixins.phantom.json",
        "mixins.phantom.refmap.json",
        "net/spartanb312/phantom/**",
        "net.spartanb312.phantom.launch.DevFMLCoreMod"
    )

    duplicatesStrategy = DuplicatesStrategy.EXCLUDE

    exclude(
        "META-INF/versions/**",
        "**/*.RSA",
        "**/*.SF",
        "**/module-info.class",
        "**/LICENSE",
        "**/*.txt"
    )

    with(tasks["jar"] as CopySpec)
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
                "FMLCorePluginContainsFMLMod" to "true",
                "FMLCorePlugin" to "net.spartanb312.phantom.launch.DevFMLCoreMod",
                "TweakClass" to "org.spongepowered.asm.launch.MixinTweaker",
                "TweakOrder" to 0,
                "ForceLoadAsMod" to "true"
            )
        }
        from(
            library.map {
                if (it.isDirectory) it
                else zipTree(it)
            }
        )
        exclude(
            "META-INF/versions/**",
            "**/*.RSA",
            "**/*.SF",
            "**/module-info.class",
            "**/LICENSE",
            "**/*.txt"
        )
    }

    "build" {
        dependsOn(client, loader)
    }
}