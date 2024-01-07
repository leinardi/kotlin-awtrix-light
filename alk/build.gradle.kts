plugins {
    alias(libs.plugins.kotlin.jvm)
    application
    alias(libs.plugins.kotlinx.serialization)
    id("detekt-conventions")
}

group = "com.leinardi.alk"
version = "unspecified"

kotlin {
    jvmToolchain(libs.versions.java.get().toInt())
}

dependencies {
    implementation(libs.bundles.kotlin.logging.jvm)
    implementation(libs.kotlinx.serialization.json)
    testImplementation(libs.kotlin.test.junit5)
}

tasks {
    withType<Test> {
        useJUnitPlatform()
    }

    named<JavaExec>("run") {
        description = "Runs the MQTT broker"
    }

    compileJava {
        options.compilerArgs.add("-Xlint:deprecation")
        options.compilerArgs.add("-Xlint:unchecked")
    }
}
