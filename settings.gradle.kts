plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.5.0"
}
// https://docs.gradle.org/7.0/userguide/declaring_dependencies.html#sec:type-safe-project-accessors
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

@Suppress("UnstableApiUsage")
dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "awtrix-light-kotlin"

includeBuild("build-conventions")
include("alk")
