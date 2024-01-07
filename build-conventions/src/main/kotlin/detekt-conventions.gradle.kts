import io.gitlab.arturbosch.detekt.Detekt
import io.gitlab.arturbosch.detekt.extensions.DetektExtension
import org.gradle.accessors.dm.LibrariesForLibs

plugins {
    id("io.gitlab.arturbosch.detekt")
}

val libs = the<LibrariesForLibs>()

detekt {
    toolVersion = libs.versions.detekt.get()
    source.from(
        DetektExtension.DEFAULT_SRC_DIR_KOTLIN,
        DetektExtension.DEFAULT_TEST_SRC_DIR_KOTLIN,
        "src/nativeMain/kotlin",
        "src/nativeTest/kotlin",
    )
    parallel = true
    autoCorrect = true
}

dependencies {
    detektPlugins(libs.detekt)
}

tasks {
    named("check") {
        dependsOn(withType<Detekt>())
    }
}
