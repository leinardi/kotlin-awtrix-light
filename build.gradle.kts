plugins {
    // Prevent loading the Kotlin Gradle plugin multiple times
    alias(libs.plugins.kotlin.jvm) apply false
    id("spotless-conventions")
    id("versions-conventions")
}

tasks {
    withType<Wrapper> {
        description = "Regenerates the Gradle Wrapper files"
        distributionType = Wrapper.DistributionType.ALL
        gradleVersion = libs.versions.gradle.get()
    }
}
